package com.example.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CrmCustomerApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String crmBaseUrl;
    private final String crmToken;
    private final String authHeaderName;
    private final String authPrefix;

    public CrmCustomerApiClient(ObjectMapper objectMapper,
                                @Value("${crm.api.base-url:http://192.168.1.65:4000}") String crmBaseUrl,
                                @Value("${crm.api.token:}") String crmToken,
                                @Value("${crm.api.auth-header-name:authorization}") String authHeaderName,
                                @Value("${crm.api.auth-prefix:Bearer }") String authPrefix) {
        this.objectMapper = objectMapper;
        this.crmBaseUrl = crmBaseUrl;
        this.crmToken = crmToken;
        this.authHeaderName = authHeaderName;
        this.authPrefix = authPrefix;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public Map<String, Object> queryCustomerInfo(String company) {
        try {
            if (crmToken == null || crmToken.trim().isEmpty()) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("success", false);
                result.put("message", "CRM token 未配置，请先通过 skill 获取并写入 token");
                return result;
            }

            String encodedCompany = URLEncoder.encode(company, StandardCharsets.UTF_8);
            String url = crmBaseUrl + "/manage/customer/mcp/info?company=" + encodedCompany;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .header(authHeaderName, authPrefix + crmToken)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("httpStatus", response.statusCode());
            result.put("requestUrl", url);

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                result.put("success", false);
                result.put("message", "CRM API request failed");
                result.put("rawBody", response.body());
                return result;
            }

            JsonNode root = objectMapper.readTree(response.body());
            result.put("success", true);
            result.put("code", root.path("code").asInt());
            result.put("msg", root.path("msg").asText());
            result.put("data", objectMapper.convertValue(root.path("data"), Object.class));
            result.put("rawBody", response.body());
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", false);
            result.put("message", "CRM API request interrupted: " + e.getMessage());
            return result;
        } catch (IOException e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", false);
            result.put("message", "CRM API request exception: " + e.getMessage());
            return result;
        }
    }
}
