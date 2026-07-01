package com.example.mcp.registry;

import com.example.mcp.annotation.McpToolDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnnotatedToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(AnnotatedToolRegistry.class);
    private static final String ALLOWED_TOOL_PACKAGE = "com.example.mcp.tool";

    private final List<RegisteredTool> tools = new ArrayList<>();
    private final Map<String, RegisteredTool> toolIndex = new ConcurrentHashMap<>();
    private final Map<Class<?>, Constructor<?>> constructorCache = new ConcurrentHashMap<>();

    public AnnotatedToolRegistry(ListableBeanFactory beanFactory) {
        beanFactory.getBeansWithAnnotation(Component.class)
                .forEach((beanName, bean) -> scanBean(bean));
        log.info("Registered {} MCP tools: {}", tools.size(),
                tools.stream().map(RegisteredTool::name).toList());
    }

    private void scanBean(Object bean) {
        Class<?> targetClass = bean.getClass();
        if (!targetClass.getPackageName().startsWith(ALLOWED_TOOL_PACKAGE)) {
            return;
        }
        for (Method method : targetClass.getMethods()) {
            McpToolDef annotation = method.getAnnotation(McpToolDef.class);
            if (annotation == null) {
                continue;
            }
            validateMethod(method, annotation);
            Class<?> inputType = method.getParameterTypes()[0];
            if (!inputType.getPackageName().startsWith(ALLOWED_TOOL_PACKAGE)) {
                throw new IllegalStateException(
                        "@McpToolDef input type must be in package " + ALLOWED_TOOL_PACKAGE
                        + ": " + inputType.getName());
            }
            var tool = new RegisteredTool(bean, method, annotation.name(), annotation.description(),
                    Set.of(annotation.optionalFields()));
            tools.add(tool);
            toolIndex.put(annotation.name(), tool);
        }
    }

    private void validateMethod(Method method, McpToolDef annotation) {
        if (method.getParameterCount() != 1) {
            throw new IllegalStateException("@McpToolDef method must have exactly one parameter: " + annotation.name());
        }
    }

    public List<Map<String, Object>> listTools() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (RegisteredTool tool : tools) {
            result.add(Map.of(
                    "name", tool.name(),
                    "description", tool.description(),
                    "inputSchema", buildInputSchema(tool)
            ));
        }
        return result;
    }

    public RegisteredTool findTool(String name) {
        return toolIndex.get(name);
    }

    public Object invoke(String toolName, Map<String, Object> arguments) {
        RegisteredTool tool = findTool(toolName);
        if (tool == null) {
            throw new IllegalArgumentException("Unsupported tool: " + toolName);
        }
        Object request = instantiateInput(tool.inputType(), arguments);
        try {
            return tool.method().invoke(tool.bean(), request);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to invoke tool: " + toolName, e);
        }
    }

    private Map<String, Object> buildInputSchema(RegisteredTool tool) {
        Class<?> inputType = tool.inputType();
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();
        var optionalSet = tool.optionalFields();

        if (inputType.isRecord()) {
            for (RecordComponent component : inputType.getRecordComponents()) {
                String fieldName = component.getName();
                properties.put(fieldName, Map.of(
                        "type", mapJsonType(component.getType()),
                        "description", fieldName
                ));
                if (!optionalSet.contains(fieldName)) {
                    required.add(fieldName);
                }
            }
        }

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        if (!required.isEmpty()) {
            schema.put("required", required);
        }
        return schema;
    }

    private Object instantiateInput(Class<?> inputType, Map<String, Object> arguments) {
        if (!inputType.isRecord()) {
            throw new IllegalStateException("Only record input types are supported: " + inputType.getName());
        }
        try {
            Constructor<?> constructor = constructorCache.computeIfAbsent(inputType, type -> {
                try {
                    RecordComponent[] components = type.getRecordComponents();
                    Class<?>[] paramTypes = new Class<?>[components.length];
                    for (int i = 0; i < components.length; i++) {
                        paramTypes[i] = components[i].getType();
                    }
                    return type.getDeclaredConstructor(paramTypes);
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("Cannot find canonical constructor for: " + type.getName(), e);
                }
            });

            RecordComponent[] components = inputType.getRecordComponents();
            Object[] parameterValues = new Object[components.length];
            for (int i = 0; i < components.length; i++) {
                parameterValues[i] = arguments.get(components[i].getName());
            }

            return constructor.newInstance(parameterValues);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate input type: " + inputType.getName(), e);
        }
    }

    private String mapJsonType(Class<?> type) {
        if (String.class.equals(type)) {
            return "string";
        }
        if (Integer.class.equals(type) || int.class.equals(type) || Long.class.equals(type) || long.class.equals(type)) {
            return "integer";
        }
        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return "boolean";
        }
        if (Number.class.isAssignableFrom(type) || double.class.equals(type) || float.class.equals(type)) {
            return "number";
        }
        return "string";
    }

    public record RegisteredTool(Object bean, Method method, String name, String description,
                                 java.util.Set<String> optionalFields) {
        public Class<?> inputType() {
            return method.getParameterTypes()[0];
        }
    }
}
