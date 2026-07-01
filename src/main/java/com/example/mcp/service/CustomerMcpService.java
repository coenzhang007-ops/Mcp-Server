package com.example.mcp.service;

import com.example.mcp.client.CrmCustomerApiClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerMcpService extends BaseMcpService {

    private final CrmCustomerApiClient crmCustomerApiClient;

    public CustomerMcpService(CrmCustomerApiClient crmCustomerApiClient) {
        this.crmCustomerApiClient = crmCustomerApiClient;
    }

    public Map<String, Object> queryCustomerInfo(String company, String accessToken) {
        Map<String, Object> result = newResult("queryCustomerInfoFromCrmTool", company);
        String token = (accessToken != null) ? accessToken.trim() : null;
        Map<String, Object> crmResponse = crmCustomerApiClient.queryCustomerInfo(
                company != null ? company.trim() : null, token);
        result.putAll(crmResponse);
        return result;
    }
}
