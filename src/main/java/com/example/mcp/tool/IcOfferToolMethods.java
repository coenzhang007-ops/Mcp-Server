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
            description = "根据型号查询offer、报价信息。返回最多50个匹配产品的列表，包含型号、品牌、供应商名称、数量、单价、dc、采购、报价时间。至少需要提供一个搜索词。"
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
