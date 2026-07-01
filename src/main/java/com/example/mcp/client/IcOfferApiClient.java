package com.example.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class IcOfferApiClient extends BaseCrmApiClient {

    public IcOfferApiClient(HttpClient httpClient,
                            ObjectMapper objectMapper,
                            @Value("${crm.api.base-url:https://api-crm.v-buy.com/uac}") String crmBaseUrl,
                            @Value("${crm.api.auth-header-name:authorization}") String authHeaderName,
                            @Value("${crm.api.auth-prefix:Bearer }") String authPrefix) {
        super(httpClient, objectMapper, crmBaseUrl, authHeaderName, authPrefix);
    }

    public Map<String, Object> queryIcOfferList(String partNo, int current, int size, String accessToken) {
        return executeWithExceptionHandling(() -> {
            Map<String, Object> result = new LinkedHashMap<>();

            if (partNo == null || partNo.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "partNo is required");
                return result;
            }
            if (partNo.trim().length() > 100) {
                result.put("success", false);
                result.put("message", "partNo too long, max 100 characters");
                return result;
            }
            int safeSize = Math.max(20, Math.min(size, 100));
            int safeCurrent = Math.max(1, current);

            String encodedPartNo = URLEncoder.encode(partNo.trim(), StandardCharsets.UTF_8);
            String url = crmBaseUrl + "/ic/offer/mcp/list?partNo=" + encodedPartNo
                    + "&size=" + safeSize + "&current=" + safeCurrent;

            HttpResponse<byte[]> response = httpClient.send(
                    buildGetRequest(url, accessToken).build(),
                    HttpResponse.BodyHandlers.ofByteArray());
            result.put("httpStatus", response.statusCode());

            CrmResponseDecoder.DecodedBody decodedBody = CrmResponseDecoder.decode(response.body(), response.headers());
            String responseText = decodedBody.text();
            log.info("请求型号：{}, HTTP状态码：{}, 响应长度：{}", partNo, response.statusCode(), responseText.length());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String trimmedBody = responseText.length() > 500 ? responseText.substring(0, 500) : responseText;
                log.warn("CRM API 返回非2xx状态码：{}，响应体前500字符：{}", response.statusCode(), trimmedBody);
                result.put("success", false);
                result.put("message", "CRM API request failed, status=" + response.statusCode());
                result.put("responseBody", trimmedBody);
                return result;
            }

            JsonNode root = objectMapper.readTree(responseText);
            result.put("success", true);
            result.put("code", root.path("code").asInt());
            result.put("msg", root.path("msg").asText());
            result.put("data", objectMapper.convertValue(root.path("data"), Object.class));
            return result;
        }, "CRM API", "partNo", partNo, log);
    }

    public Map<String, Object> queryIcRfqList(String partNo, int current, int size, String accessToken) {
        return executeWithExceptionHandling(() -> {
            Map<String, Object> result = new LinkedHashMap<>();

            if (partNo == null || partNo.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "partNo is required");
                return result;
            }
            if (partNo.trim().length() > 100) {
                result.put("success", false);
                result.put("message", "partNo too long, max 100 characters");
                return result;
            }
            int safeSize = Math.max(20, Math.min(size, 100));
            int safeCurrent = Math.max(1, current);

            String encodedPartNo = URLEncoder.encode(partNo.trim(), StandardCharsets.UTF_8);
            String url = crmBaseUrl + "/ic/rfq/mcp/list?partNo=" + encodedPartNo
                    + "&size=" + safeSize + "&current=" + safeCurrent;

            HttpResponse<byte[]> response = httpClient.send(
                    buildGetRequest(url, accessToken).build(),
                    HttpResponse.BodyHandlers.ofByteArray());
            result.put("httpStatus", response.statusCode());

            CrmResponseDecoder.DecodedBody decodedBody = CrmResponseDecoder.decode(response.body(), response.headers());
            String responseText = decodedBody.text();
            log.info("请求型号：{}, HTTP状态码：{}, 响应长度：{}", partNo, response.statusCode(), responseText.length());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String trimmedBody = responseText.length() > 500 ? responseText.substring(0, 500) : responseText;
                log.warn("CRM API 返回非2xx状态码：{}，响应体前500字符：{}", response.statusCode(), trimmedBody);
                result.put("success", false);
                result.put("message", "CRM API request failed, status=" + response.statusCode());
                result.put("responseBody", trimmedBody);
                return result;
            }

            JsonNode root = objectMapper.readTree(responseText);
            result.put("success", true);
            result.put("code", root.path("code").asInt());
            result.put("msg", root.path("msg").asText());
            result.put("data", objectMapper.convertValue(root.path("data"), Object.class));
            return result;
        }, "CRM API", "partNo", partNo, log);
    }
}
