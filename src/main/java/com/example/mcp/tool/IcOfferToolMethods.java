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
            description = "根据型号查询offer、报价信息。返回最多50个匹配产品的列表，包含型号、品牌、供应商名称、数量、单价、dc、采购、报价时间。至少需要提供一个搜索词。"
    )
    public Map<String, Object> queryIcOfferList(IcOfferListRequest request) {
        return icOfferMcpService.queryIcOfferList(request.partNo(), request.current(), request.size(), request.accessToken());
    }

    public record IcOfferListRequest(String partNo, int current, int size, String accessToken) {
    }
}
