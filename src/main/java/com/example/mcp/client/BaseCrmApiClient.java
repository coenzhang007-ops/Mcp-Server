package com.example.mcp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;

/**
 * CRM API 客户端抽象基类，封装共享的 HTTP 配置、认证头和异常处理工具方法。
 */
public abstract class BaseCrmApiClient {

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;
    protected final String crmBaseUrl;
    protected final String authHeaderName;
    protected final String authPrefix;

    protected BaseCrmApiClient(ObjectMapper objectMapper,
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

    /**
     * 构建带 Accept 和 Authorization 头的 GET 请求 Builder。
     */
    protected HttpRequest.Builder buildGetRequest(String url, String accessToken) {
        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header(authHeaderName, authPrefix + (accessToken != null ? accessToken : ""));
    }

    /**
     * 安全获取异常消息，避免 null。
     */
    protected static String safeMessage(Throwable throwable) {
        if (throwable == null) {
            return "unknown";
        }
        if (throwable.getMessage() != null && !throwable.getMessage().trim().isEmpty()) {
            return throwable.getMessage();
        }
        return throwable.toString();
    }

    /**
     * 填充异常信息到结果 Map。
     */
    protected static Map<String, Object> exceptionResult(Map<String, Object> result, String prefix, Exception e) {
        result.put("success", false);
        result.put("message", prefix + ": " + safeMessage(e));
        result.put("exceptionType", e.getClass().getName());
        return result;
    }
}
