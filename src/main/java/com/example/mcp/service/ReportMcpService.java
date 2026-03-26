package com.example.mcp.service;

import com.example.mcp.client.CrmReportApiClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReportMcpService {

    private final CrmReportApiClient crmReportApiClient;

    public ReportMcpService(CrmReportApiClient crmReportApiClient) {
        this.crmReportApiClient = crmReportApiClient;
    }

    public Map<String, Object> callPostTool(String toolName, String path, Map<String, Object> body, String accessToken) {
        return crmReportApiClient.post(toolName, path, body, accessToken);
    }

    public Map<String, Object> callGetTool(String toolName, String path, String accessToken) {
        return crmReportApiClient.get(toolName, path, accessToken);
    }
}
