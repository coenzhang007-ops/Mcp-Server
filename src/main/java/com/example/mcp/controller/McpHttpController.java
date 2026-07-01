package com.example.mcp.controller;

import com.example.mcp.registry.AnnotatedToolRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/mcp", produces = MediaType.APPLICATION_JSON_VALUE)
public class McpHttpController {

    private static final Logger log = LoggerFactory.getLogger(McpHttpController.class);

    private final AnnotatedToolRegistry annotatedToolRegistry;
    private final String apiKey;

    public McpHttpController(AnnotatedToolRegistry annotatedToolRegistry,
                             @Value("${mcp-server.api-key:changeme}") String apiKey) {
        this.annotatedToolRegistry = annotatedToolRegistry;
        this.apiKey = apiKey;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handle(@RequestBody Map<String, Object> request,
                                       HttpServletRequest httpRequest) {
        String requestApiKey = httpRequest.getHeader("X-API-Key");
//        if (requestApiKey == null || !requestApiKey.equals(apiKey)) {
//            log.warn("Invalid or missing API key from IP: {}", httpRequest.getRemoteAddr());
//            return error(null, -32001, "Unauthorized: invalid or missing API key");
//        }

        Object id = request.get("id");
        String method = request.get("method") instanceof String value ? value : null;
        Map<String, Object> params = request.get("params") instanceof Map<?, ?> rawParams
                ? castMap(rawParams)
                : Map.of();

        try {
            return switch (method) {
                case "initialize" -> success(id, Map.of(
                        "protocolVersion", "2024-11-05",
                        "capabilities", Map.of(
                                "tools", Map.of()
                        ),
                        "serverInfo", Map.of(
                                "name", "mcp-server-demo",
                                "version", "1.0.0"
                        )
                ));
                case "notifications/initialized" -> success(id, Map.of());
                case "tools/list" -> success(id, Map.of(
                        "tools", annotatedToolRegistry.listTools()
                ));
                case "tools/call" -> handleToolCall(id, params);
                default -> error(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            log.error("MCP request failed: method={}, id={}", method, id, e);
            return error(id, -32603, "Internal server error");
        }
    }

    private Map<String, Object> handleToolCall(Object id, Map<String, Object> params) {
        String name = params.get("name") instanceof String value ? value : null;
        Map<String, Object> arguments = params.get("arguments") instanceof Map<?, ?> rawArgs
                ? castMap(rawArgs)
                : Map.of();

        Object toolResult;
        try {
            toolResult = annotatedToolRegistry.invoke(name, arguments);
        } catch (IllegalArgumentException e) {
            return error(id, -32602, e.getMessage());
        }

        return success(id, Map.of(
                "content", List.of(Map.of(
                        "type", "text",
                        "text", String.valueOf(toolResult)
                )),
                "structuredContent", toolResult,
                "isError", Boolean.FALSE
        ));
    }

    private Map<String, Object> success(Object id, Object result) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        if (id != null) {
            response.put("id", id);
        }
        response.put("result", result);
        return response;
    }

    private Map<String, Object> error(Object id, int code, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        if (id != null) {
            response.put("id", id);
        }
        response.put("error", Map.of(
                "code", code,
                "message", message
        ));
        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Map<?, ?> source) {
        return (Map<String, Object>) source;
    }
}
