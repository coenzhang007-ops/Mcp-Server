package com.example.mcp.service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseMcpService {

    protected Map<String, Object> newResult(String toolName, Object keyParam) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tool", toolName);
        result.put("timestamp", Instant.now().toString());
        if (keyParam != null) {
            result.put("param", keyParam);
        }
        return result;
    }

    protected Map<String, Object> fail(Map<String, Object> result, String message) {
        result.put("success", false);
        result.put("message", message);
        return result;
    }

    protected Map<String, Object> failWithEmptyData(Map<String, Object> result, String message) {
        result.put("success", false);
        result.put("message", message);
        result.put("data", java.util.List.of());
        return result;
    }
}
