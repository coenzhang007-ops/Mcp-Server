package com.example.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpHttpSseConfig {

    @Value("${mcp.public-base-url:http://8.147.68.53:8088}")
    private String publicBaseUrl;

    @Bean
    public HttpServletSseServerTransportProvider httpServletSseServerTransportProvider(ObjectMapper objectMapper) {
        return HttpServletSseServerTransportProvider.builder()
                .objectMapper(objectMapper)
                .baseUrl(publicBaseUrl)
                .messageEndpoint("/mcp")
                .sseEndpoint("/mcp")
                .build();
    }

    @Bean
    public ServletRegistrationBean<HttpServletSseServerTransportProvider> mcpSseServletRegistration(
            HttpServletSseServerTransportProvider transportProvider) {
        ServletRegistrationBean<HttpServletSseServerTransportProvider> registrationBean =
                new ServletRegistrationBean<>(transportProvider, "/mcp");
        registrationBean.setName("mcpStreamableHttpServlet");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
}
