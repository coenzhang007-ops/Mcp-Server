package com.example.mcp.tool;

import com.example.mcp.annotation.McpToolDef;
import com.example.mcp.service.IcOfferMcpService;
import com.example.mcp.service.IcRfqMcpService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IcRfqToolMethods {

    private final IcRfqMcpService icRfqMcpService;

    public IcRfqToolMethods(IcRfqMcpService icRfqMcpService) {
        this.icRfqMcpService = icRfqMcpService;
    }

    @McpToolDef(
            name = "queryIcRfqListFromCrmTool",
            description = "根据型号查询rfq、询价信息。返回最多50个匹配产品的列表，包含型号、品牌、客户名称、数量、单价、dc、采购、报价时间。至少需要提供一个搜索词。"
    )
    public Map<String, Object> queryIcRfqList(IcOfferListRequest request) {
        return icRfqMcpService.queryIcRfqList(request.partNo(), request.current(), request.size(), request.accessToken());
    }


    public record IcOfferListRequest(String partNo, int current, int size, String accessToken) {
    }
}
