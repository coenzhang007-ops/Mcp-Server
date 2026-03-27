package com.example.mcp.client;

import java.net.http.HttpHeaders;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class CrmResponseDecoder {

    private CrmResponseDecoder() {
    }

    public static DecodedBody decode(byte[] bodyBytes, HttpHeaders headers) {
        String contentType = headers.firstValue("Content-Type").orElse("");
        String declaredCharset = extractCharset(contentType).orElse(null);

        Map<String, Object> diagnostics = new LinkedHashMap<>();
        diagnostics.put("contentType", contentType);
        diagnostics.put("declaredCharset", declaredCharset);
        diagnostics.put("bodySize", bodyBytes == null ? 0 : bodyBytes.length);

        if (bodyBytes == null || bodyBytes.length == 0) {
            diagnostics.put("usedCharset", StandardCharsets.UTF_8.name());
            diagnostics.put("decodeStrategy", "empty-body");
            return new DecodedBody("", diagnostics);
        }

        if (declaredCharset != null) {
            try {
                Charset charset = Charset.forName(declaredCharset);
                String text = new String(bodyBytes, charset);
                diagnostics.put("usedCharset", charset.name());
                diagnostics.put("decodeStrategy", "declared-charset");
                return new DecodedBody(text, diagnostics);
            } catch (Exception ignored) {
                diagnostics.put("declaredCharsetError", "Unsupported or invalid declared charset: " + declaredCharset);
            }
        }

        String utf8Text = new String(bodyBytes, StandardCharsets.UTF_8);
        boolean utf8LooksBroken = looksLikeMojibake(utf8Text);
        diagnostics.put("utf8LooksBroken", utf8LooksBroken);

        if (!utf8LooksBroken) {
            diagnostics.put("usedCharset", StandardCharsets.UTF_8.name());
            diagnostics.put("decodeStrategy", "utf8-default");
            return new DecodedBody(utf8Text, diagnostics);
        }

        String gbkText = new String(bodyBytes, Charset.forName("GBK"));
        boolean gbkLooksBroken = looksLikeMojibake(gbkText);
        diagnostics.put("gbkLooksBroken", gbkLooksBroken);

        if (!gbkLooksBroken) {
            diagnostics.put("usedCharset", "GBK");
            diagnostics.put("decodeStrategy", "gbk-fallback");
            return new DecodedBody(gbkText, diagnostics);
        }

        diagnostics.put("usedCharset", StandardCharsets.UTF_8.name());
        diagnostics.put("decodeStrategy", "utf8-fallback-even-though-suspicious");
        return new DecodedBody(utf8Text, diagnostics);
    }

    private static Optional<String> extractCharset(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return Optional.empty();
        }
        String lower = contentType.toLowerCase(Locale.ROOT);
        int idx = lower.indexOf("charset=");
        if (idx < 0) {
            return Optional.empty();
        }
        String raw = contentType.substring(idx + "charset=".length()).trim();
        int semicolonIdx = raw.indexOf(';');
        if (semicolonIdx >= 0) {
            raw = raw.substring(0, semicolonIdx).trim();
        }
        raw = raw.replace("\"", "").trim();
        if (raw.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(raw);
    }

    private static boolean looksLikeMojibake(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        int suspicious = 0;
        int sample = Math.min(text.length(), 3000);
        for (int i = 0; i < sample; i++) {
            char c = text.charAt(i);
            if (c == '\uFFFD' || c == '�') {
                suspicious += 3;
            } else if (c == '?' && containsCjk(text)) {
                suspicious += 1;
            }
        }
        return suspicious >= 3;
    }

    private static boolean containsCjk(String text) {
        int sample = Math.min(text.length(), 3000);
        for (int i = 0; i < sample; i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(text.charAt(i));
            if (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
                return true;
            }
        }
        return false;
    }

    public record DecodedBody(String text, Map<String, Object> diagnostics) {
    }
}
