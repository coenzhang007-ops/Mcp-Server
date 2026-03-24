# Java MCP Server Demo

项目路径：`D:\Project\mcp`

这是一个最小可用的 **Java MCP Server**，现在同时支持两种传输模式：

## 1) 纯 HTTP JSON-RPC

- `POST /mcp`
- 适合 Postman、普通 HTTP client、自定义集成

## 2) HTTP + SSE

- `GET /sse`
- `POST /mcp/message`
- 适合基于 SSE 的 MCP client

## 默认监听地址

- `http://127.0.0.1:8088`

## 已实现方法

- `initialize`
- `notifications/initialized`
- `tools/list`
- `tools/call`

当前提供工具：

- `queryCustomerInfoFromCrmTool`

## 启动方式

```bash
cd /d D:\Project\mcp
mvn clean package
java -jar target\mcp-server-demo-1.0.0-SNAPSHOT.jar
```
