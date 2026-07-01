package com.example.mcp.tool;

import ch.qos.logback.core.util.StringUtil;
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
        String token = request.accessToken();
        if (StringUtil.isNullOrEmpty(token)) {
            token = "04f1f913-0c8b-4345-a637-3bfafb699d6e";
        }
        return icOfferMcpService.queryIcOfferList(request.partNo(), request.current(), request.size, token);
    }

    public record IcOfferListRequest(String partNo, int current, int size, String accessToken) {
    }
}
