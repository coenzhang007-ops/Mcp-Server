package com.example.mcp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CrmReportApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String crmBaseUrl;
    private final String authHeaderName;
    private final String authPrefix;

    public CrmReportApiClient(ObjectMapper objectMapper,
                              @Value("${crm.api.base-url:http://192.168.1.250:9999/uac}") String crmBaseUrl,
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

    public Map<String, Object> post(String toolName, String path, Map<String, Object> body, String accessToken) {
        return exchange(toolName, "POST", path, body, accessToken);
    }

    public Map<String, Object> get(String toolName, String path, String accessToken) {
        return exchange(toolName, "GET", path, null, accessToken);
    }

    private Map<String, Object> exchange(String toolName, String method, String path, Map<String, Object> body, String accessToken) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tool", toolName);
        result.put("timestamp", LocalDateTime.now().toString());
        result.put("crmBaseUrl", crmBaseUrl);
        result.put("path", path);
        result.put("method", method);
        result.put("authHeaderName", authHeaderName);

        if (accessToken == null || accessToken.isBlank()) {
            result.put("success", false);
            result.put("message", "accessToken is required");
            result.put("data", List.of());
            return result;
        }

        try {
            String url = crmBaseUrl + path;
            result.put("requestUrl", url);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/json")
                    .header(authHeaderName, authPrefix + accessToken.trim());

            if ("POST".equalsIgnoreCase(method)) {
                String jsonBody = body == null ? "{}" : objectMapper.writeValueAsString(body);
                result.put("requestBody", body == null ? Map.of() : body);
                builder.header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));
            } else {
                builder.GET();
            }

            HttpResponse<byte[]> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            result.put("httpStatus", response.statusCode());
            result.put("responseHeaders", response.headers().map());

            CrmResponseDecoder.DecodedBody decodedBody = CrmResponseDecoder.decode(response.body(), response.headers());
            String responseText = decodedBody.text();
            result.put("decodeDiagnostics", decodedBody.diagnostics());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                result.put("success", false);
                result.put("message", "CRM API request failed");
                result.put("rawBody", responseText);
                return result;
            }

            Map<String, Object> parsed = parseJsonObject(responseText);
            result.put("success", true);
            result.put("response", parsed);
            result.put("code", parsed.get("code"));
            result.put("msg", parsed.get("msg"));
            result.put("data", parsed.get("data"));
            result.put("rawBody", responseText);
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
        } catch (JsonProcessingException e) {
            result.put("success", false);
            result.put("message", "Failed to serialize request body: " + safeMessage(e));
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

    private Map<String, Object> parseJsonObject(String body) throws JsonProcessingException {
        if (body == null || body.isBlank()) {
            return Map.of();
        }
        return objectMapper.readValue(body, new TypeReference<>() {});
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
