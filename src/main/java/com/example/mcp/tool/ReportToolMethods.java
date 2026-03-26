package com.example.mcp.tool;

import com.example.mcp.annotation.McpToolDef;
import com.example.mcp.service.ReportMcpService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ReportToolMethods {

    private final ReportMcpService reportMcpService;

    public ReportToolMethods(ReportMcpService reportMcpService) {
        this.reportMcpService = reportMcpService;
    }

    @McpToolDef(name = "crmHomePageOnlineCountTool", description = "Get CRM homepage online user count. Requires accessToken.")
    public Map<String, Object> crmHomePageOnlineCountTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmHomePageOnlineCountTool", "/manage/home/online/count", request.accessToken());
    }

    @McpToolDef(name = "crmHomePageFollowCountTool", description = "Get CRM homepage follow count. Body fields usually include startDate, endDate, queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageFollowCountTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageFollowCountTool", "/manage/home/follow/count", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageFollowRankingTool", description = "Get CRM homepage follow ranking. Body fields usually include startDate, endDate, queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageFollowRankingTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageFollowRankingTool", "/manage/home/follow/ranking", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageRfqCountTool", description = "Get CRM homepage RFQ count. Body fields usually include startDate, endDate, scopeType, queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageRfqCountTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageRfqCountTool", "/manage/home/rfq/count", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageRfqRankingTool", description = "Get CRM homepage RFQ ranking. Body fields usually include startDate, endDate, scopeType, queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageRfqRankingTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageRfqRankingTool", "/manage/home/rfq/ranking", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageCustomerCountTool", description = "Get CRM homepage customer funnel count. Body fields usually include startDate, endDate, queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageCustomerCountTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageCustomerCountTool", "/manage/home/customer/count", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageCustomerCategoryTool", description = "Get CRM homepage customer category pie chart data. Body fields usually include startDate, endDate, queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageCustomerCategoryTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageCustomerCategoryTool", "/manage/home/customer/category", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageOrderCustomerTool", description = "Get CRM homepage order customer trend. Body fields usually include type and queryType. Requires accessToken.")
    public Map<String, Object> crmHomePageOrderCustomerTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageOrderCustomerTool", "/manage/home/order/customer", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmHomePageOrderCountTool", description = "Get CRM homepage sales summary count. Requires accessToken.")
    public Map<String, Object> crmHomePageOrderCountTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmHomePageOrderCountTool", "/manage/home/order/count", request.accessToken());
    }

    @McpToolDef(name = "crmHomePageNewestNoticeTool", description = "Get CRM homepage latest notice. Requires accessToken.")
    public Map<String, Object> crmHomePageNewestNoticeTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmHomePageNewestNoticeTool", "/manage/home/notice/new", request.accessToken());
    }

    @McpToolDef(name = "crmHomePageCurrentMonthScheduleTool", description = "Get CRM homepage current month schedule. Requires accessToken.")
    public Map<String, Object> crmHomePageCurrentMonthScheduleTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmHomePageCurrentMonthScheduleTool", "/manage/home/schedule/currentMonth", request.accessToken());
    }

    @McpToolDef(name = "crmHomePageClearScheduleTool", description = "Clear CRM homepage schedule. Body fields may include id or birthdayDate. Requires accessToken.")
    public Map<String, Object> crmHomePageClearScheduleTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmHomePageClearScheduleTool", "/manage/home/schedule/clear", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportHomeQuickTool", description = "Get CRM report home quick summary. Body may include userIds, company, dateType, startDate, endDate, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportHomeQuickTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportHomeQuickTool", "/admin/report/home/quick", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportHomeNewContactTool", description = "Get CRM report home new contact summary. Requires accessToken.")
    public Map<String, Object> crmReportHomeNewContactTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportHomeNewContactTool", "/admin/report/home/newcontact", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportHomeRfqCustomerCountTool", description = "Get CRM report home RFQ customer count summary. Requires accessToken.")
    public Map<String, Object> crmReportHomeRfqCustomerCountTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportHomeRfqCustomerCountTool", "/admin/report/home/rfq/customer/count", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportHomeQuickTwoTool", description = "Get CRM report home quick summary two. Requires accessToken.")
    public Map<String, Object> crmReportHomeQuickTwoTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportHomeQuickTwoTool", "/admin/report/home/quick/two", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportHomeImproveTool", description = "Get CRM report home improve summary. Requires accessToken.")
    public Map<String, Object> crmReportHomeImproveTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportHomeImproveTool", "/admin/report/home/improve", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDistributionTool", description = "Get CRM customer distribution report. Body may include company, userIds, startDate, endDate, status, drawSource, dataField, dataSort, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportDistributionTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDistributionTool", "/admin/report/distribution", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDrawTool", description = "Get CRM draw customer report. Requires accessToken.")
    public Map<String, Object> crmReportDrawTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDrawTool", "/admin/report/draw", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDrawDetailTool", description = "Get CRM draw customer report detail. Requires accessToken.")
    public Map<String, Object> crmReportDrawDetailTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDrawDetailTool", "/admin/report/draw/detail", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportRemoveDetailTool", description = "Get CRM remove customer detail report. Requires accessToken.")
    public Map<String, Object> crmReportRemoveDetailTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportRemoveDetailTool", "/admin/report/remove/detail", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportFollowTool", description = "Get CRM follow report. Body may include company, contact, type, followType, startDate, endDate, dataField, dataSort, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportFollowTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportFollowTool", "/admin/report/follow", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportPerfectDetailTool", description = "Get CRM perfect customer detail report. Body may include sortType, sort, userIds, startDate, endDate, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportPerfectDetailTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportPerfectDetailTool", "/admin/report/perfect/detail", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportRemoveExportBooleanTool", description = "Check whether CRM remove export has data. Requires accessToken.")
    public Map<String, Object> crmReportRemoveExportBooleanTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportRemoveExportBooleanTool", "/admin/report/remove/export/boolean", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportWaitHandleQueryTool", description = "Get CRM wait handle warnings. Requires accessToken.")
    public Map<String, Object> crmReportWaitHandleQueryTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callPostTool("crmReportWaitHandleQueryTool", "/admin/report/wait/handle/query", Map.of(), request.accessToken());
    }

    @McpToolDef(name = "crmReportWarningQueryTool", description = "Get CRM warning query result. Requires accessToken.")
    public Map<String, Object> crmReportWarningQueryTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callPostTool("crmReportWarningQueryTool", "/admin/report/warning/query", Map.of(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailDeptReportDataTool", description = "Get CRM department customer detail statistics. Body may include userIds, startDate, endDate. Requires accessToken.")
    public Map<String, Object> crmReportDetailDeptReportDataTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailDeptReportDataTool", "/admin/report/detail/dept/report/data", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailToPublicTool", description = "Get CRM to-public detail data. Body may include userIds, startDate, endDate, dataField, dataSort, company, phone, type, keyword, level, area, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportDetailToPublicTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailToPublicTool", "/admin/report/detail/to/public", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailToPublicInitTool", description = "Initialize CRM to-public data. Requires accessToken.")
    public Map<String, Object> crmReportDetailToPublicInitTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmReportDetailToPublicInitTool", "/admin/report/detail/to/public/init", request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailAddContactTool", description = "Get CRM added contact report. Body may include userIds, startDate, endDate, dataField, dataSort, contactKey, contactEmFax, customerName, job, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportDetailAddContactTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailAddContactTool", "/admin/report/detail/add/contact", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailAddCustomerTool", description = "Get CRM added customer report. Body may include userIds, startDate, endDate, dataField, dataSort, keyword, area, level, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportDetailAddCustomerTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailAddCustomerTool", "/admin/report/detail/add/customer", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailManageInfoTool", description = "Get CRM manage data statistics. Body may include idList, startDate, endDate, dataField, dataSort, dataList, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportDetailManageInfoTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailManageInfoTool", "/admin/report/detail/manage/info", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailDeptInfoTool", description = "Get CRM department data statistics. Same body style as manage info. Requires accessToken.")
    public Map<String, Object> crmReportDetailDeptInfoTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailDeptInfoTool", "/admin/report/detail/dept/info", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailUserInfoTool", description = "Get CRM user data statistics. Same body style as manage info. Requires accessToken.")
    public Map<String, Object> crmReportDetailUserInfoTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailUserInfoTool", "/admin/report/detail/user/info", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailManageSelectTool", description = "Get CRM manage select dropdown data. Requires accessToken.")
    public Map<String, Object> crmReportDetailManageSelectTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmReportDetailManageSelectTool", "/admin/report/detail/manage/select", request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailFollowCustomerDetailTool", description = "Get CRM weekly follow customer detail report. Body may include userIds, companyName, contacts, followType, ratingCode, category, demBrand, startDate, endDate, categorySelectList, isE, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmReportDetailFollowCustomerDetailTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailFollowCustomerDetailTool", "/admin/report/detail/followCustomerDetail", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailFollowTaskCustomerDetailTool", description = "Get CRM assigned follow task customer detail report. Same body as followCustomerDetail. Requires accessToken.")
    public Map<String, Object> crmReportDetailFollowTaskCustomerDetailTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailFollowTaskCustomerDetailTool", "/admin/report/detail/followTaskCustomerDetail", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailFilterBoxListTool", description = "Get CRM report detail filter box list. Body.type is appended as query string. Requires accessToken.")
    public Map<String, Object> crmReportDetailFilterBoxListTool(TypeQueryRequest request) {
        return reportMcpService.callGetTool("crmReportDetailFilterBoxListTool", "/admin/report/detail/filter/box/list?type=" + encode(request.type()), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailSetFilterBoxTool", description = "Set CRM report detail filter box configuration. Body may include type, dataListStr, dataList. Requires accessToken.")
    public Map<String, Object> crmReportDetailSetFilterBoxTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailSetFilterBoxTool", "/admin/report/detail/set/filter/box", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailCustomerBoxListTool", description = "Get CRM customer box list. Requires type and accessToken.")
    public Map<String, Object> crmReportDetailCustomerBoxListTool(TypeQueryRequest request) {
        return reportMcpService.callGetTool("crmReportDetailCustomerBoxListTool", "/admin/report/detail/customer/box/list?type=" + encode(request.type()), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailCustomerBoxUpdateTool", description = "Update CRM customer box config. Body may include type, dataListStr, dataList. Requires accessToken.")
    public Map<String, Object> crmReportDetailCustomerBoxUpdateTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmReportDetailCustomerBoxUpdateTool", "/admin/report/detail/customer/box/update", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailCustomerBoxInitUpdateTool", description = "Initialize CRM customer box update. Requires accessToken.")
    public Map<String, Object> crmReportDetailCustomerBoxInitUpdateTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callPostTool("crmReportDetailCustomerBoxInitUpdateTool", "/admin/report/detail/customer/box/init/update", Map.of(), request.accessToken());
    }

    @McpToolDef(name = "crmReportDetailCustomerBoxListInitUpdateTool", description = "Initialize CRM customer box list update. Requires accessToken.")
    public Map<String, Object> crmReportDetailCustomerBoxListInitUpdateTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callPostTool("crmReportDetailCustomerBoxListInitUpdateTool", "/admin/report/detail/customer/box/list/init/update", Map.of(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerSourceListTool", description = "Get CRM sales data customer source list. Body usually includes userIdList, beginDate, endDate. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerSourceListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerSourceListTool", "/sales/data/report/customer/source/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerRegionListTool", description = "Get CRM sales data customer region list. Body may include userIdList, beginDate, endDate, sortField, sortType. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerRegionListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerRegionListTool", "/sales/data/report/customer/region/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerCategoryListTool", description = "Get CRM sales data customer category list. Body may include userIdList, beginDate, endDate, sortField, sortType. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerCategoryListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerCategoryListTool", "/sales/data/report/customer/category/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerOrderAmountListTool", description = "Get CRM customer order amount list. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerOrderAmountListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerOrderAmountListTool", "/sales/data/report/customer/order/amount/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerOrderCountListTool", description = "Get CRM customer order count list. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerOrderCountListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerOrderCountListTool", "/sales/data/report/customer/order/count/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerOrderTotalByOrderTool", description = "Get CRM order total statistics by order. Body may include userIdList, orderNo, orderType, company, area, contactName, keyword, beginDate, endDate, page, pageSize, dataField, dataSort. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerOrderTotalByOrderTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerOrderTotalByOrderTool", "/sales/data/report/customer/order/total/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerOrderTotalCountByOrderTool", description = "Get CRM order total amount statistics by order. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerOrderTotalCountByOrderTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerOrderTotalCountByOrderTool", "/sales/data/report/customer/order/total/count/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerOrderTotalByPartTool", description = "Get CRM order total statistics by part. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerOrderTotalByPartTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerOrderTotalByPartTool", "/sales/data/report/customer/order/total/list/by/part", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportCustomerOrderTotalCountByPartTool", description = "Get CRM order total count statistics by part. Requires accessToken.")
    public Map<String, Object> crmSalesReportCustomerOrderTotalCountByPartTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportCustomerOrderTotalCountByPartTool", "/sales/data/report/customer/order/total/count/list/by/part", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportRfqRecordListTool", description = "Get CRM RFQ record list. Body may include keyword, rfqType, company, contactName, beginDate, endDate, page, pageSize, dataField, dataSort. Requires accessToken.")
    public Map<String, Object> crmSalesReportRfqRecordListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportRfqRecordListTool", "/sales/data/report/customer/rfq/record/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportTrendSummaryDataTool", description = "Get CRM RFQ trend summary data. Body may include keyWord, type, sortField, sortType, startDate, endDate, brandNames, customerIds, partNo, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmSalesReportTrendSummaryDataTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportTrendSummaryDataTool", "/sales/data/report/rfq/trend/summary/data", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportIncreasePartNoCountListTool", description = "Get CRM increasing part number trend detail. Requires accessToken.")
    public Map<String, Object> crmSalesReportIncreasePartNoCountListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportIncreasePartNoCountListTool", "/sales/data/report/rfq/increase/trend/partNo/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportDecreasePartNoCountListTool", description = "Get CRM decreasing part number trend detail. Requires accessToken.")
    public Map<String, Object> crmSalesReportDecreasePartNoCountListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportDecreasePartNoCountListTool", "/sales/data/report/rfq/decrease/trend/partNo/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportTrendDetailListTool", description = "Get CRM RFQ trend detail list. Requires accessToken.")
    public Map<String, Object> crmSalesReportTrendDetailListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportTrendDetailListTool", "/sales/data/report/rfq/trend/detail/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportTrendDetailCustomerListTool", description = "Get CRM RFQ trend detail customer list. Requires accessToken.")
    public Map<String, Object> crmSalesReportTrendDetailCustomerListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportTrendDetailCustomerListTool", "/sales/data/report/rfq/trend/detail/customer/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportRfqBrandCountListTool", description = "Get CRM RFQ brand count list. Requires accessToken.")
    public Map<String, Object> crmSalesReportRfqBrandCountListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportRfqBrandCountListTool", "/sales/data/report/rfq/brand/count/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportRfqBrandSelectListTool", description = "Get CRM RFQ brand select list. Requires accessToken.")
    public Map<String, Object> crmSalesReportRfqBrandSelectListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportRfqBrandSelectListTool", "/sales/data/report/rfq/brand/select/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportRfqCustomerSelectListTool", description = "Get CRM RFQ customer select list. Requires accessToken.")
    public Map<String, Object> crmSalesReportRfqCustomerSelectListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportRfqCustomerSelectListTool", "/sales/data/report/rfq/customer/select/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportRfqPartNoTrendListTool", description = "Get CRM RFQ part number trend list. Body may include partNo, startDate, endDate, userIdList, ownerIdList. Requires accessToken.")
    public Map<String, Object> crmSalesReportRfqPartNoTrendListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportRfqPartNoTrendListTool", "/sales/data/report/rfq/partNo/trend/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmSalesReportAnalyseTool", description = "Get CRM customer analyse statistics. Body may include ownerList, listType, roleFlag, sort, sortType. Requires accessToken.")
    public Map<String, Object> crmSalesReportAnalyseTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmSalesReportAnalyseTool", "/sales/data/report/analyse", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpBuyerMemberListTool", description = "Get CRM GP buyer member report. Body may include name, begin, end, memberSortType, memberSort, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmGpBuyerMemberListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpBuyerMemberListTool", "/gp/report/buyer/member/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpBuyerTeamListTool", description = "Get CRM GP buyer team report. Body may include team, begin, end, teamSortType, teamSort, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmGpBuyerTeamListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpBuyerTeamListTool", "/gp/report/buyer/team/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpSalesMemberListTool", description = "Get CRM GP sales member report. Requires accessToken.")
    public Map<String, Object> crmGpSalesMemberListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpSalesMemberListTool", "/gp/report/sales/member/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpSalesTeamListTool", description = "Get CRM GP sales team report. Requires accessToken.")
    public Map<String, Object> crmGpSalesTeamListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpSalesTeamListTool", "/gp/report/sales/team/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpChannelSalesMemberListTool", description = "Get CRM GP channel sales member report. Requires accessToken.")
    public Map<String, Object> crmGpChannelSalesMemberListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpChannelSalesMemberListTool", "/gp/report/channel/sales/member/list", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpSendEmailBuyerTool", description = "Trigger CRM GP buyer report email sending. Body may include begin and end. Requires accessToken.")
    public Map<String, Object> crmGpSendEmailBuyerTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpSendEmailBuyerTool", "/gp/report/send/email/buyer", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpSendEmailSalesTool", description = "Trigger CRM GP sales report email sending. Requires accessToken.")
    public Map<String, Object> crmGpSendEmailSalesTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpSendEmailSalesTool", "/gp/report/send/email/sales", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpSendEmailChannelSalesTool", description = "Trigger CRM GP channel sales report email sending. Requires accessToken.")
    public Map<String, Object> crmGpSendEmailChannelSalesTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpSendEmailChannelSalesTool", "/gp/report/send/email/channel/sales", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpEmailListBuyerTool", description = "Get CRM GP buyer email record list. Requires accessToken.")
    public Map<String, Object> crmGpEmailListBuyerTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpEmailListBuyerTool", "/gp/report/email/list/buyer", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpEmailListSalesTool", description = "Get CRM GP sales email record list. Requires accessToken.")
    public Map<String, Object> crmGpEmailListSalesTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpEmailListSalesTool", "/gp/report/email/list/sales", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpEmailListChannelSalesTool", description = "Get CRM GP channel sales email record list. Requires accessToken.")
    public Map<String, Object> crmGpEmailListChannelSalesTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmGpEmailListChannelSalesTool", "/gp/report/email/list/channel/sales", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmGpExcelTemplateDownloadTool", description = "Get CRM GP report excel template download URL. Requires accessToken.")
    public Map<String, Object> crmGpExcelTemplateDownloadTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmGpExcelTemplateDownloadTool", "/gp/report/excel/template/download", request.accessToken());
    }

    @McpToolDef(name = "crmGpProcessingDataTool", description = "Trigger CRM GP report data processing. Requires accessToken.")
    public Map<String, Object> crmGpProcessingDataTool(AccessTokenOnlyRequest request) {
        return reportMcpService.callGetTool("crmGpProcessingDataTool", "/gp/report/date/text", request.accessToken());
    }

    @McpToolDef(name = "crmOrderMaintenanceSelectFormTool", description = "Query CRM order maintenance detail list. Body may include orderDateStart, orderDateEnd, orderNo, brand, partNo, customerName, customerCode, customerOrderNo, sellCurrency, sellOrderNo, accountPeriod, supplier, procureDateStart, procureDateEnd, procureOrderNo, buyCurrency, batch, procureAccountPeriod, mode, type, saleName, saleDeptName, manager, isSell, originalBuyPriceFlag, isProcure, isProcureOrder, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmOrderMaintenanceSelectFormTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmOrderMaintenanceSelectFormTool", "/admin/order/selectFrom", request.body(), request.accessToken());
    }

    @McpToolDef(name = "crmOrderMaintenanceDeleteByIdsTool", description = "Delete CRM order maintenance records by ids. Body should be a JSON object containing ids list under a compatible wrapper only if your backend gateway adapts it; otherwise this tool may not apply. Requires accessToken.")
    public Map<String, Object> crmOrderMaintenanceDeleteByIdsTool(GenericPostRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.putAll(request.body());
        return reportMcpService.callPostTool("crmOrderMaintenanceDeleteByIdsTool", "/admin/order/delectByIds", body, request.accessToken());
    }

    @McpToolDef(name = "crmStatisticalAddCustomerContactListTool", description = "Get CRM statistical added customer contact list. Body may include startDate, endDate, page, pageSize. Requires accessToken.")
    public Map<String, Object> crmStatisticalAddCustomerContactListTool(GenericPostRequest request) {
        return reportMcpService.callPostTool("crmStatisticalAddCustomerContactListTool", "/statistical/report/sales/add/customerConcat/list", request.body(), request.accessToken());
    }

    private String encode(String value) {
        if (value == null) {
            return "";
        }
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }

    public record AccessTokenOnlyRequest(String accessToken) {
    }

    public record TypeQueryRequest(String type, String accessToken) {
    }

    public record GenericPostRequest(Map<String, Object> body, String accessToken) {
        public GenericPostRequest {
            if (body == null) {
                body = Map.of();
            }
        }
    }
}
