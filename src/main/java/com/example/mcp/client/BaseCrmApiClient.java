package com.example.mcp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CRM API 客户端抽象基类，封装共享的 HTTP 配置、认证头、异常处理模板方法和响应解析工具。
 * <p>
 * 配置值由子类通过 @Value 注入并传入，父类本身不是 Spring Bean。
 */
public abstract class BaseCrmApiClient {

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;
    protected final String crmBaseUrl;
    protected final String authHeaderName;
    protected final String authPrefix;
    protected final Duration requestTimeout;

    protected BaseCrmApiClient(HttpClient httpClient,
                               ObjectMapper objectMapper,
                               String crmBaseUrl,
                               String authHeaderName,
                               String authPrefix) {
        this(httpClient, objectMapper, crmBaseUrl, authHeaderName, authPrefix, Duration.ofSeconds(10));
    }

    protected BaseCrmApiClient(HttpClient httpClient,
                               ObjectMapper objectMapper,
                               String crmBaseUrl,
                               String authHeaderName,
                               String authPrefix,
                               Duration requestTimeout) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.crmBaseUrl = crmBaseUrl;
        this.authHeaderName = authHeaderName;
        this.authPrefix = authPrefix;
        this.requestTimeout = requestTimeout;
    }

    /**
     * 构建带 Accept 和 Authorization 头的 GET 请求 Builder。
     */
    protected HttpRequest.Builder buildGetRequest(String url, String accessToken) {
        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .GET()
                .timeout(requestTimeout)
                .header("Accept", "application/json")
                .header(authHeaderName, authPrefix + (accessToken != null ? accessToken : ""));
    }

    /**
     * 统一异常处理模板方法，消除子类中的重复 catch 块。
     *
     * @param callable  核心业务逻辑（可能抛出 IOException / InterruptedException）
     * @param logPrefix 日志前缀，如 "CRM API"
     * @param paramKey  参数标识，如 "company" 或 "partNo"
     * @param paramValue 参数值，用于日志记录
     * @param log       Slf4j Logger 实例
     * @return 包含业务结果或异常信息的结果 Map
     */
    protected Map<String, Object> executeWithExceptionHandling(
            CrmApiCallable callable,
            String logPrefix,
            String paramKey,
            String paramValue,
            Logger log) {
        try {
            return callable.call();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", false);
            result.put("message", logPrefix + " request interrupted: " + safeMessage(e));
            result.put("exceptionType", e.getClass().getName());
            return result;
        } catch (ConnectException e) {
            log.warn("{} connect failed, {}={}", logPrefix, paramKey, paramValue, e);
            return exceptionResult(new LinkedHashMap<>(), logPrefix + " connect failed", e);
        } catch (UnknownHostException e) {
            log.warn("{} unknown host, {}={}", logPrefix, paramKey, paramValue, e);
            return exceptionResult(new LinkedHashMap<>(), logPrefix + " unknown host", e);
        } catch (IOException e) {
            log.error("{} IO exception, {}={}", logPrefix, paramKey, paramValue, e);
            return exceptionResult(new LinkedHashMap<>(), logPrefix + " IO exception", e);
        } catch (Exception e) {
            log.error("{} unexpected exception, {}={}", logPrefix, paramKey, paramValue, e);
            Map<String, Object> result = exceptionResult(
                    new LinkedHashMap<>(), logPrefix + " unexpected exception", e);
            if (e.getCause() != null) {
                result.put("causeType", e.getCause().getClass().getName());
                result.put("causeMessage", safeMessage(e.getCause()));
            }
            return result;
        }
    }

    /**
     * CRM API 调用函数式接口，允许抛出受检异常。
     */
    @FunctionalInterface
    protected interface CrmApiCallable {
        Map<String, Object> call() throws IOException, InterruptedException;
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
