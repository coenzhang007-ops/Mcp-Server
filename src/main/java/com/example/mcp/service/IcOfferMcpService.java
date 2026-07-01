package com.example.mcp.service;

import com.example.mcp.client.IcOfferApiClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IcOfferMcpService extends BaseMcpService {

    private final IcOfferApiClient icOfferApiClient;

    public IcOfferMcpService(IcOfferApiClient icOfferApiClient) {
        this.icOfferApiClient = icOfferApiClient;
    }

    public Map<String, Object> queryIcOfferList(String partNo, int current, int size, String accessToken) {
        Map<String, Object> result = newResult("queryIcOfferListFromCrmTool", partNo);
        String token = (accessToken != null) ? accessToken.trim() : null;
        Map<String, Object> crmResponse = icOfferApiClient.queryIcOfferList(
                partNo != null ? partNo.trim() : null, current, size, token);
        result.putAll(crmResponse);
        return result;
    }

    public Map<String, Object> queryIcRfqList(String partNo, int current, int size, String accessToken) {
        Map<String, Object> result = newResult("queryIcRfqListFromCrmTool", partNo);
        String token = (accessToken != null) ? accessToken.trim() : null;
        Map<String, Object> crmResponse = icOfferApiClient.queryIcRfqList(
                partNo != null ? partNo.trim() : null, current, size, token);
        result.putAll(crmResponse);
        return result;
    }
}
