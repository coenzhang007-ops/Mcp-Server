package com.example.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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
    private final String authHeaderName;
    private final String authPrefix;

    public CrmCustomerApiClient(ObjectMapper objectMapper,
                                @Value("${crm.api.base-url:https://api-crm.v-buy.com/uac}") String crmBaseUrl,
                                @Value("${crm.api.auth-header-name:authorization}") String authHeaderName,
                                @Value("${crm.api.auth-prefix:Bearer }") String authPrefix) {
        this.objectMapper = objectMapper;
        this.crmBaseUrl = crmBaseUrl;
        this.authHeaderName = authHeaderName;
        this.authPrefix = authPrefix;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public Map<String, Object> queryCustomerInfo(String company, String accessToken) {
        Map<String, Object> result = new LinkedHashMap<>();

        try {
            String safeToken = (accessToken != null) ? accessToken.trim() : "";
            String encodedCompany = URLEncoder.encode(company, StandardCharsets.UTF_8);
            String url = crmBaseUrl + "/manage/customer/mcp/info?company=" + encodedCompany;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .header(authHeaderName, authPrefix + safeToken)
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            result.put("httpStatus", response.statusCode());

            CrmResponseDecoder.DecodedBody decodedBody = CrmResponseDecoder.decode(response.body(), response.headers());
            String responseText = decodedBody.text();

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                result.put("success", false);
                result.put("message", "CRM API request failed");
                return result;
            }

            JsonNode root = objectMapper.readTree(responseText);
            result.put("success", true);
            result.put("code", root.path("code").asInt());
            result.put("msg", root.path("msg").asText());
            result.put("data", objectMapper.convertValue(root.path("data"), Object.class));
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.put("success", false);
            result.put("message", "CRM API request interrupted: " + safeMessage(e));
            result.put("exceptionType", e.getClass().getName());
            return result;
        } catch (ConnectException e) {
            result.put("success", false);
            result.put("message", "CRM API connect failed: " + safeMessage(e));
            result.put("exceptionType", e.getClass().getName());
            return result;
        } catch (UnknownHostException e) {
            result.put("success", false);
            result.put("message", "CRM API unknown host: " + safeMessage(e));
            result.put("exceptionType", e.getClass().getName());
            return result;
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "CRM API IO exception: " + safeMessage(e));
            result.put("exceptionType", e.getClass().getName());
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "CRM API unexpected exception: " + safeMessage(e));
            result.put("exceptionType", e.getClass().getName());
            if (e.getCause() != null) {
                result.put("causeType", e.getCause().getClass().getName());
                result.put("causeMessage", safeMessage(e.getCause()));
            }
            return result;
        }
    }

    private String safeMessage(Throwable throwable) {
        if (throwable == null) {
            return "unknown";
        }
        if (throwable.getMessage() != null && !throwable.getMessage().trim().isEmpty()) {
            return throwable.getMessage();
        }
        return throwable.toString();
    }
}
