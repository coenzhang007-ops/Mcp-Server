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
public class CrmCustomerApiClient extends BaseCrmApiClient {

    public CrmCustomerApiClient(HttpClient httpClient,
                                ObjectMapper objectMapper,
                                @Value("${crm.api.base-url:https://api-crm.v-buy.com/uac}") String crmBaseUrl,
                                @Value("${crm.api.auth-header-name:authorization}") String authHeaderName,
                                @Value("${crm.api.auth-prefix:Bearer }") String authPrefix) {
        super(httpClient, objectMapper, crmBaseUrl, authHeaderName, authPrefix);
    }

    public Map<String, Object> queryCustomerInfo(String company, String accessToken) {
        return executeWithExceptionHandling(() -> {
            Map<String, Object> result = new LinkedHashMap<>();

            if (company == null || company.isBlank()) {
                result.put("success", false);
                result.put("message", "company is required");
                return result;
            }
            if (company.length() > 200) {
                result.put("success", false);
                result.put("message", "company name too long, max 200 characters");
                return result;
            }
            String encodedCompany = URLEncoder.encode(company.trim(), StandardCharsets.UTF_8);
            String url = crmBaseUrl + "/manage/customer/mcp/info?company=" + encodedCompany;

            HttpResponse<byte[]> response = httpClient.send(
                    buildGetRequest(url, accessToken).build(),
                    HttpResponse.BodyHandlers.ofByteArray());
            result.put("httpStatus", response.statusCode());

            CrmResponseDecoder.DecodedBody decodedBody = CrmResponseDecoder.decode(response.body(), response.headers());
            String responseText = decodedBody.text();
            log.info("查询客户：{}, HTTP状态码：{}, 响应长度：{}", company, response.statusCode(), responseText.length());

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
        }, "CRM API", "company", company, log);
    }
}
