package com.example.mcp.service;

import com.example.mcp.client.IcOfferApiClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class IcOfferMcpService {

    private final IcOfferApiClient icOfferApiClient;

    public IcOfferMcpService(IcOfferApiClient icOfferApiClient) {
        this.icOfferApiClient = icOfferApiClient;
    }

    public Map<String, Object> queryIcOfferList(String partNo, int current, String accessToken) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tool", "queryIcOfferListFromCrmTool");
        result.put("timestamp", LocalDateTime.now().toString());
        result.put("partNo", partNo);
        result.put("current", current);

        if (partNo == null || partNo.isBlank()) {
            result.put("success", false);
            result.put("message", "partNo is required");
            result.put("data", List.of());
            return result;
        }

        if (accessToken == null || accessToken.isBlank()) {
            result.put("success", false);
            result.put("message", "accessToken is required");
            result.put("data", List.of());
            return result;
        }

        Map<String, Object> crmResponse = icOfferApiClient.queryIcOfferList(partNo.trim(), current, accessToken.trim());
        result.putAll(crmResponse);
        return result;
    }
}
