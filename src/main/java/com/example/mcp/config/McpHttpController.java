package com.example.mcp.config;

import com.example.mcp.service.CustomerMcpService;
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

    private final CustomerMcpService customerMcpService;

    public McpHttpController(CustomerMcpService customerMcpService) {
        this.customerMcpService = customerMcpService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handle(@RequestBody Map<String, Object> request) {
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
                        "tools", List.of(Map.of(
                                "name", "queryCustomerInfoFromCrmTool",
                                "description", "Call CRM endpoint /manage/customer/mcp/info to query customer information by company name",
                                "inputSchema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "company", Map.of(
                                                        "type", "string",
                                                        "description", "Company name"
                                                )
                                        ),
                                        "required", List.of("company")
                                )
                        ))
                ));
                case "tools/call" -> handleToolCall(id, params);
                default -> error(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            return error(id, -32603, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        }
    }

    private Map<String, Object> handleToolCall(Object id, Map<String, Object> params) {
        String name = params.get("name") instanceof String value ? value : null;
        Map<String, Object> arguments = params.get("arguments") instanceof Map<?, ?> rawArgs
                ? castMap(rawArgs)
                : Map.of();

        if (!"queryCustomerInfoFromCrmTool".equals(name)) {
            return error(id, -32602, "Unsupported tool: " + name);
        }

        String company = arguments.get("company") instanceof String value ? value : null;
        Map<String, Object> data = customerMcpService.queryCustomerInfo(company);

        return success(id, Map.of(
                "content", List.of(Map.of(
                        "type", "text",
                        "text", data.toString()
                )),
                "structuredContent", data,
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
