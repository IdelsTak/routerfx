package com.github.idelstak.routerfx.proof;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.*;
import java.security.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public final class HttpRouterApi implements RouterApi {
    private static final String CGI_PATH = "/cgi-bin/http.cgi";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";
    private static final String X_REQUESTED_WITH = "XMLHttpRequest";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final URI cgiUri;
    private final String language;

    public HttpRouterApi(String baseUrl) {
        this(baseUrl, "en", false);
    }

    public HttpRouterApi(String baseUrl, String language, boolean enableCookies) {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(language, "language must not be null");

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(enableCookies ? CookiePolicy.ACCEPT_ALL : CookiePolicy.ACCEPT_NONE);

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .cookieHandler(cookieManager)
                .build();
        this.mapper = new ObjectMapper();
        this.cgiUri = URI.create(baseUrl).resolve(CGI_PATH);
        this.language = language;
    }

    @Override
    public Challenge fetchChallenge() {
        ObjectNode req = mapper.createObjectNode();
        req.put("cmd", 232);
        req.put("method", "GET");
        req.put("sessionId", "");
        req.put("language", language);

        JsonNode res = post(req);
        String token = requiredText(res, "token");
        return new Challenge(token);
    }

    @Override
    public Session login(Credentials credentials, Challenge challenge) {
        Objects.requireNonNull(credentials, "credentials must not be null");
        Objects.requireNonNull(challenge, "challenge must not be null");

        String preLoginSessionId = generatePreLoginSessionId();
        String passwd = sha256Hex(challenge.token() + credentials.password());

        ObjectNode req = mapper.createObjectNode();
        req.put("cmd", 100);
        req.put("method", "POST");
        req.put("sessionId", preLoginSessionId);
        req.put("username", credentials.username());
        req.put("passwd", passwd);
        req.put("isAutoUpgrade", "0");
        req.put("language", language);

        JsonNode res = post(req);
        if ("fail".equals(res.path("login_fail").asText()) || "fail".equals(res.path("login_fail2").asText())) {
            throw new RouterProtocolException("Login rejected: " + res.toString());
        }

        String authSessionId = requiredText(res, "sessionId");
        return new Session(authSessionId);
    }

    @Override
    public RadioState fetchRadioState(Session session) {
        Objects.requireNonNull(session, "session must not be null");

        ObjectNode req = mapper.createObjectNode();
        req.put("cmd", 205);
        req.put("method", "GET");
        req.put("language", language);
        req.put("sessionId", session.sessionId());

        JsonNode res = post(req);
        return new RadioState(
                requiredText(res, "network_operator"),
                requiredText(res, "network_type_str"),
                requiredText(res, "RSRP"),
                requiredText(res, "RSSI"),
                requiredText(res, "RSRQ"),
                requiredText(res, "SINR"),
                requiredText(res, "currentband"),
                requiredText(res, "bandwidth"),
                requiredText(res, "flow_dl"),
                requiredText(res, "flow_ul"),
                requiredText(res, "onlineTime"),
                requiredText(res, "onlineDuration")
        );
    }

    private JsonNode post(ObjectNode requestJson) {
        String requestText;
        try {
            requestText = mapper.writeValueAsString(requestJson);
        } catch (JsonProcessingException e) {
            throw new RouterProtocolException("Failed to serialize request JSON", e);
        }

        HttpRequest request = HttpRequest.newBuilder(cgiUri)
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", CONTENT_TYPE)
                .header("X-Requested-With", X_REQUESTED_WITH)
                .POST(HttpRequest.BodyPublishers.ofString(requestText, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RouterProtocolException("HTTP request failed", e);
        }

        if (response.statusCode() != 200) {
            throw new RouterProtocolException("Unexpected HTTP status: " + response.statusCode());
        }

        String jsonText = trimToJsonObject(response.body());
        JsonNode node;
        try {
            node = mapper.readTree(jsonText);
        } catch (JsonProcessingException e) {
            throw new RouterProtocolException("Failed to parse response JSON", e);
        }

        if (!node.isObject()) {
            throw new RouterProtocolException("Router response is not a JSON object");
        }

        if (node.has("success") && !node.path("success").asBoolean()) {
            String message = node.path("message").asText("Router returned success=false");
            throw new RouterProtocolException(message + " | body=" + node);
        }

        return node;
    }

    private static String trimToJsonObject(String raw) {
        if (raw == null) {
            throw new RouterProtocolException("Empty response body");
        }
        int idx = raw.indexOf('{');
        if (idx < 0) {
            throw new RouterProtocolException("Response does not contain a JSON object");
        }
        return raw.substring(idx).trim();
    }

    private static String requiredText(JsonNode node, String fieldName) {
        JsonNode v = node.get(fieldName);
        if (v == null || v.isNull()) {
            throw new RouterProtocolException("Missing required field: " + fieldName + " in " + node);
        }
        String text = v.asText();
        if (text == null || text.isBlank()) {
            throw new RouterProtocolException("Blank required field: " + fieldName + " in " + node);
        }
        return text;
    }

    private static String generatePreLoginSessionId() {
        return md5Hex(randomStringLikeJs()) + md5Hex(randomStringLikeJs());
    }

    private static String randomStringLikeJs() {
        return Double.toString(ThreadLocalRandom.current().nextDouble());
    }

    private static String sha256Hex(String input) {
        return digestHex("SHA-256", input);
    }

    private static String md5Hex(String input) {
        return digestHex("MD5", input);
    }

    private static String digestHex(String algorithm, String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(Character.forDigit((b >>> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RouterProtocolException("Missing digest algorithm: " + algorithm, e);
        }
    }
}
