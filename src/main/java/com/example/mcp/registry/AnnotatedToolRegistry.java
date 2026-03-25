package com.example.mcp.registry;

import com.example.mcp.annotation.McpToolDef;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnotatedToolRegistry {

    private final List<RegisteredTool> tools = new ArrayList<>();

    public AnnotatedToolRegistry(ListableBeanFactoryProvider beanFactoryProvider) {
        beanFactoryProvider.getBeans().forEach((beanName, bean) -> scanBean(bean));
    }

    private void scanBean(Object bean) {
        Class<?> targetClass = bean.getClass();
        for (Method method : targetClass.getMethods()) {
            McpToolDef annotation = method.getAnnotation(McpToolDef.class);
            if (annotation == null) {
                continue;
            }
            validateMethod(method, annotation);
            tools.add(new RegisteredTool(bean, method, annotation.name(), annotation.description()));
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
                    "inputSchema", buildInputSchema(tool.inputType())
            ));
        }
        return result;
    }

    public RegisteredTool findTool(String name) {
        return tools.stream()
                .filter(tool -> tool.name().equals(name))
                .findFirst()
                .orElse(null);
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

    private Map<String, Object> buildInputSchema(Class<?> inputType) {
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();

        if (inputType.isRecord()) {
            for (RecordComponent component : inputType.getRecordComponents()) {
                properties.put(component.getName(), Map.of(
                        "type", mapJsonType(component.getType()),
                        "description", component.getName()
                ));
                required.add(component.getName());
            }
        }

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", required);
        return schema;
    }

    private Object instantiateInput(Class<?> inputType, Map<String, Object> arguments) {
        if (!inputType.isRecord()) {
            throw new IllegalStateException("Only record input types are supported: " + inputType.getName());
        }
        try {
            RecordComponent[] components = inputType.getRecordComponents();
            Class<?>[] parameterTypes = new Class<?>[components.length];
            Object[] parameterValues = new Object[components.length];

            for (int i = 0; i < components.length; i++) {
                RecordComponent component = components[i];
                parameterTypes[i] = component.getType();
                parameterValues[i] = arguments.get(component.getName());
            }

            return inputType.getDeclaredConstructor(parameterTypes).newInstance(parameterValues);
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

    public record RegisteredTool(Object bean, Method method, String name, String description) {
        public Class<?> inputType() {
            return method.getParameterTypes()[0];
        }
    }
}
