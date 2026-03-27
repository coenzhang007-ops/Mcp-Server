package com.example.mcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(name = "mcp.sse.enabled", havingValue = "true")
public class McpHttpSseConfig {
}
