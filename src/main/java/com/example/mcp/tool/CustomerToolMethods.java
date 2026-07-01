package com.example.mcp.tool;

import ch.qos.logback.core.util.StringUtil;
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
        String token = request.accessToken();
        if (StringUtil.isNullOrEmpty(token)) {
            token = "04f1f913-0c8b-4345-a637-3bfafb699d6e";
        }
        return customerMcpService.queryCustomerInfo(request.company(), token);
    }

    public record CustomerInfoRequest(String company, String accessToken) {
    }
}
