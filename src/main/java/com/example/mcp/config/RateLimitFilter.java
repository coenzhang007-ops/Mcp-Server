package com.example.mcp.config;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Order(1)
public class RateLimitFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final long WINDOW_MS = 60_000;

    private final ConcurrentHashMap<String, SlidingWindow> counterMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String clientIp = getClientIp(httpRequest);
            long now = System.currentTimeMillis();
            SlidingWindow window = counterMap.compute(clientIp, (k, v) -> {
                if (v == null || now - v.windowStart() > WINDOW_MS) {
                    return new SlidingWindow(now, new AtomicInteger(1));
                }
                return v;
            });

            if (window != null && window.windowStart() + WINDOW_MS > now
                    && window.counter().incrementAndGet() - 1 > MAX_REQUESTS_PER_MINUTE) {
                log.warn("Rate limit exceeded for IP: {}, count: {}", clientIp, window.counter().get());
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(429);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write(
                        "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32000,\"message\":\"Rate limit exceeded. Max 60 requests per minute.\"}}");
                return;
            }
        }
        chain.doFilter(request, response);
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

    private record SlidingWindow(AtomicLong windowStart, AtomicInteger counter) {
        long windowStart() {
            return windowStart.get();
        }
    }
}
