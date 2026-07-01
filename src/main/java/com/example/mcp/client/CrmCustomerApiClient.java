package com.example.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CrmCustomerApiClient extends BaseCrmApiClient {

    public CrmCustomerApiClient(ObjectMapper objectMapper,
                                @Value("${crm.api.base-url:https://api-crm.v-buy.com/uac}") String crmBaseUrl,
                                @Value("${crm.api.auth-header-name:authorization}") String authHeaderName,
                                @Value("${crm.api.auth-prefix:Bearer }") String authPrefix) {
        super(objectMapper, crmBaseUrl, authHeaderName, authPrefix);
    }

    public Map<String, Object> queryCustomerInfo(String company, String accessToken) {
        Map<String, Object> result = new LinkedHashMap<>();

        try {
            if (company == null || company.isBlank()) {
                result.put("success", false);
                result.put("message", "company is required");
                return result;
            }
            if (company.length() > 200) {
                result.put("success", false);
                result.put("message", "company name too long, max 200 characters");
                return result;
            }
            String encodedCompany = URLEncoder.encode(company.trim(), StandardCharsets.UTF_8);
            String url = crmBaseUrl + "/manage/customer/mcp/info?company=" + encodedCompany;

            HttpResponse<byte[]> response = httpClient.send(
                    buildGetRequest(url, accessToken).build(),
                    HttpResponse.BodyHandlers.ofByteArray());
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
            return exceptionResult(result, "CRM API connect failed", e);
        } catch (UnknownHostException e) {
            return exceptionResult(result, "CRM API unknown host", e);
        } catch (IOException e) {
            return exceptionResult(result, "CRM API IO exception", e);
        } catch (Exception e) {
            result = exceptionResult(result, "CRM API unexpected exception", e);
            if (e.getCause() != null) {
                result.put("causeType", e.getCause().getClass().getName());
                result.put("causeMessage", safeMessage(e.getCause()));
            }
            return result;
        }
    }
}
