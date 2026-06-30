package com.example.mcp.tool;

import com.example.mcp.annotation.McpToolDef;
import com.example.mcp.service.IcOfferMcpService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IcOfferToolMethods {

    private final IcOfferMcpService icOfferMcpService;

    public IcOfferToolMethods(IcOfferMcpService icOfferMcpService) {
        this.icOfferMcpService = icOfferMcpService;
    }

    @McpToolDef(
            name = "queryIcOfferListFromCrmTool",
            description = "Query IC offer list by part number from CRM. Requires accessToken from crm-auth-skill. Use this when the user asks to search IC offers, component pricing, or part number quotes such as 查询INA236AIDDFR的报价offer."
    )
    public Map<String, Object> queryIcOfferList(IcOfferListRequest request) {
        return icOfferMcpService.queryIcOfferList(request.partNo(), request.current(), request.accessToken());
    }

    public record IcOfferListRequest(String partNo, int current, String accessToken) {
    }
}
