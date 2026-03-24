package com.example.mcp.tool;

import com.example.mcp.service.CustomerMcpService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CustomerMcpTools {

    private final CustomerMcpService customerMcpService;

    public CustomerMcpTools(CustomerMcpService customerMcpService) {
        this.customerMcpService = customerMcpService;
    }

    @Bean
    public ToolCallback queryCustomerInfoFromCrmTool() {
        return FunctionToolCallback.builder("queryCustomerInfoFromCrmTool", (CustomerInfoRequest request) ->
                        customerMcpService.queryCustomerInfo(request.company()))
                .description("Call CRM endpoint /manage/customer/mcp/info to query customer information by company name")
                .inputType(CustomerInfoRequest.class)
                .build();
    }

    public record CustomerInfoRequest(String company) {
    }
}
