package com.example.mcp.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IP-based rate limiting filter using Caffeine cache for automatic entry expiration.
 * Limits each client IP to {@value #MAX_REQUESTS_PER_MINUTE} requests per minute.
 */
@Component
@Order(1)
public class RateLimitFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Cache<String, AtomicInteger> counterCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String clientIp = getClientIp(httpRequest);

            AtomicInteger counter = counterCache.get(clientIp, k -> new AtomicInteger(0));
            int currentCount = counter.incrementAndGet();

            if (currentCount > MAX_REQUESTS_PER_MINUTE) {
                log.warn("Rate limit exceeded for IP: {}, count: {}", clientIp, currentCount);

                // Extract JSON-RPC id from request body for proper error response
                Object requestId = extractJsonRpcId(httpRequest);

                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(429);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write(buildRateLimitError(requestId));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Attempt to extract the JSON-RPC "id" field from the request body.
     * Returns null if parsing fails.
     */
    private Object extractJsonRpcId(HttpServletRequest request) {
        try {
            JsonNode root = objectMapper.readTree(request.getInputStream());
            JsonNode idNode = root.get("id");
            if (idNode == null || idNode.isNull()) {
                return null;
            }
            if (idNode.isTextual()) {
                return idNode.asText();
            }
            if (idNode.isNumber()) {
                return idNode.asLong();
            }
            return idNode.asText();
        } catch (Exception e) {
            return null;
        }
    }

    private String buildRateLimitError(Object requestId) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("{\"jsonrpc\":\"2.0\"");
        if (requestId != null) {
            sb.append(",\"id\":");
            if (requestId instanceof Number) {
                sb.append(requestId);
            } else {
                sb.append('"').append(requestId).append('"');
            }
        }
        sb.append(",\"error\":{\"code\":-32000,\"message\":\"Rate limit exceeded. Max ")
                .append(MAX_REQUESTS_PER_MINUTE)
                .append(" requests per minute.\"}}");
        return sb.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
