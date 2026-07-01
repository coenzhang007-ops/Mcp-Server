package com.example.mcp.service;

import com.example.mcp.client.IcRfqApiClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IcRfqMcpService extends BaseMcpService {

    private final IcRfqApiClient icRfqApiClient;

    public IcRfqMcpService(IcRfqApiClient icRfqApiClient) {
        this.icRfqApiClient = icRfqApiClient;
    }


    public Map<String, Object> queryIcRfqList(String partNo, int current, int size, String accessToken) {
        Map<String, Object> result = newResult("queryIcRfqListFromCrmTool", partNo);
        String token = (accessToken != null) ? accessToken.trim() : null;
        Map<String, Object> crmResponse = icRfqApiClient.queryIcRfqList(
                partNo != null ? partNo.trim() : null, current, size, token);
        result.putAll(crmResponse);
        return result;
    }
}
