package com.example.mcp.service;

import com.example.mcp.client.CrmCustomerApiClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerMcpService {

    private final CrmCustomerApiClient crmCustomerApiClient;

    public CustomerMcpService(CrmCustomerApiClient crmCustomerApiClient) {
        this.crmCustomerApiClient = crmCustomerApiClient;
    }

    public Map<String, Object> queryCustomerInfo(String company) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tool", "queryCustomerInfoFromCrmTool");
        result.put("timestamp", LocalDateTime.now().toString());
        result.put("company", company);

        if (company == null || company.isBlank()) {
            result.put("success", false);
            result.put("message", "company is required");
            result.put("data", List.of());
            return result;
        }

        Map<String, Object> crmResponse = crmCustomerApiClient.queryCustomerInfo(company.trim());
        result.putAll(crmResponse);
        return result;
    }
}
