package com.example.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpHttpSseConfig {

    @Bean
    public HttpServletSseServerTransportProvider httpServletSseServerTransportProvider(ObjectMapper objectMapper) {
        return HttpServletSseServerTransportProvider.builder()
                .objectMapper(objectMapper)
                .baseUrl("http://127.0.0.1:8088")
                .messageEndpoint("/mcp/message")
                .sseEndpoint("/sse")
                .build();
    }

    @Bean
    public ServletRegistrationBean<HttpServletSseServerTransportProvider> mcpSseServletRegistration(
            HttpServletSseServerTransportProvider transportProvider) {
        ServletRegistrationBean<HttpServletSseServerTransportProvider> registrationBean =
                new ServletRegistrationBean<>(transportProvider, "/sse", "/mcp/message");
        registrationBean.setName("mcpSseTransportServlet");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
}
