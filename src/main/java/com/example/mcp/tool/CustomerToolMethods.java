package com.example.mcp.tool;

import com.example.mcp.annotation.McpToolDef;
import com.example.mcp.service.CustomerMcpService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomerToolMethods {

    private final CustomerMcpService customerMcpService;

    public CustomerToolMethods(CustomerMcpService customerMcpService) {
        this.customerMcpService = customerMcpService;
    }

    @McpToolDef(
            name = "queryCustomerInfoFromCrmTool",
            description = "Query CRM customer information by company name or partial keyword. Requires accessToken from crm-auth-skill. Use this when the user asks to search CRM customers, company profiles, or customer info such as 查询包含vadas的客户信息."
    )
    public Map<String, Object> queryCustomerInfo(CustomerInfoRequest request) {
        return customerMcpService.queryCustomerInfo(request.company(), request.accessToken());
    }

    public record CustomerInfoRequest(String company, String accessToken) {
    }
}
