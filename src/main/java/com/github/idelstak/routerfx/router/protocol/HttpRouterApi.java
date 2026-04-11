package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.*;
import java.security.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public final class HttpRouterApi implements RouterApi {

    private final String cgiPath;
    private final String contentType;
    private final String requestedWith;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final URI cgiUri;
    private final String language;
    private final RouterProtocolRequests requests;
    private final RouterProtocolResponses responses;

    public HttpRouterApi(String baseUrl) {
        this(baseUrl, "en", false);
    }

    public HttpRouterApi(String baseUrl, String language, boolean enableCookies) {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(language, "language must not be null");

        var cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(enableCookies ? CookiePolicy.ACCEPT_ALL
                                        : CookiePolicy.ACCEPT_NONE);

        this.httpClient = HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(5))
          .cookieHandler(cookieManager)
          .build();
        this.cgiPath = "/cgi-bin/http.cgi";
        this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        this.requestedWith = "XMLHttpRequest";
        this.mapper = new ObjectMapper();
        this.cgiUri = URI.create(baseUrl).resolve(this.cgiPath);
        this.language = language;
        this.requests = new RouterProtocolRequests(this.mapper, this.language);
        this.responses = new RouterProtocolResponses();
    }

    HttpRouterApi(String baseUrl, String language, HttpClient httpClient, ObjectMapper mapper) {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(language, "language must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.cgiPath = "/cgi-bin/http.cgi";
        this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        this.requestedWith = "XMLHttpRequest";
        this.mapper = Objects.requireNonNull(mapper, "mapper must not be null");
        this.cgiUri = URI.create(baseUrl).resolve(this.cgiPath);
        this.language = language;
        this.requests = new RouterProtocolRequests(this.mapper, this.language);
        this.responses = new RouterProtocolResponses();
    }

    @Override
    public Challenge fetchChallenge() {
        return responses.toChallenge(post(requests.challenge()));
    }

    @Override
    public Session login(Credentials credentials, Challenge challenge) {
        Objects.requireNonNull(credentials, "credentials must not be null");
        Objects.requireNonNull(challenge, "challenge must not be null");

        var preLoginSessionId = generatePreLoginSessionId();
        var passwd = sha256Hex(challenge.token() + credentials.password());

        return responses.toSession(post(requests.login(credentials.username(), passwd, preLoginSessionId)));
    }

    @Override
    public RadioState fetchRadioState(Session session) {
        Objects.requireNonNull(session, "session must not be null");

        return responses.toRadioState(post(requests.radio(session.sessionId())));
    }

    private JsonNode post(ObjectNode requestJson) {
        var requestText = serialize(requestJson);
        var request = HttpRequest.newBuilder(cgiUri)
          .timeout(Duration.ofSeconds(15))
          .header("Content-Type", contentType)
          .header("X-Requested-With", requestedWith)
          .POST(HttpRequest.BodyPublishers.ofString(requestText, StandardCharsets.UTF_8))
          .build();
        var response = send(request);

        if (response.statusCode() != 200) {
            throw new RouterProtocolException("Unexpected HTTP status: " + response.statusCode());
        }

        var jsonText = trimToJsonObject(response.body());
        var node = parse(jsonText);

        if (!node.isObject()) {
            throw new RouterProtocolException("Router response is not a JSON object");
        }

        if (node.has("success") && !node.path("success").asBoolean()) {
            var message = node.path("message").asText("Router returned success=false");
            throw new RouterProtocolException(message + " | body=" + node);
        }

        return node;
    }

    private String serialize(ObjectNode requestJson) {
        try {
            return mapper.writeValueAsString(requestJson);
        } catch (JsonProcessingException e) {
            throw new RouterProtocolException("Failed to serialize request JSON", e);
        }
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RouterProtocolException("HTTP request failed", e);
        }
    }

    private JsonNode parse(String jsonText) {
        try {
            return mapper.readTree(jsonText);
        } catch (JsonProcessingException e) {
            throw new RouterProtocolException("Failed to parse response JSON", e);
        }
    }

    private String trimToJsonObject(String raw) {
        if (raw == null) {
            throw new RouterProtocolException("Empty response body");
        }
        var idx = raw.indexOf('{');
        if (idx < 0) {
            throw new RouterProtocolException("Response does not contain a JSON object");
        }
        return raw.substring(idx).trim();
    }

    private String generatePreLoginSessionId() {
        return md5Hex(randomStringLikeJs()) + md5Hex(randomStringLikeJs());
    }

    private String randomStringLikeJs() {
        return Double.toString(ThreadLocalRandom.current().nextDouble());
    }

    private String sha256Hex(String input) {
        return digestHex("SHA-256", input);
    }

    private String md5Hex(String input) {
        return digestHex("MD5", input);
    }

    private String digestHex(String algorithm, String input) {
        try {
            var md = MessageDigest.getInstance(algorithm);
            var digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            var sb = new StringBuilder(digest.length * 2);
            for (var b : digest) {
                sb.append(Character.forDigit((b >>> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RouterProtocolException("Missing digest algorithm: " + algorithm, e);
        }
    }
}
