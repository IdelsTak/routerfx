package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.github.idelstak.routerfx.shared.result.*;
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
    public Result<Challenge> fetchChallenge() {
        return post(requests.challenge()).flatMap(responses::challenge);
    }

    @Override
    public Result<Session> login(Credentials credentials, Challenge challenge) {
        Objects.requireNonNull(credentials, "credentials must not be null");
        Objects.requireNonNull(challenge, "challenge must not be null");

        var preLoginSessionId = generatePreLoginSessionId();
        var passwd = sha256Hex(challenge.token() + credentials.password());

        return post(requests.login(credentials.username(), passwd, preLoginSessionId)).flatMap(responses::session);
    }

    @Override
    public Result<RadioState> fetchRadioState(Session session) {
        Objects.requireNonNull(session, "session must not be null");

        return post(requests.radio(session.sessionId())).flatMap(responses::radio);
    }

    private Result<JsonNode> post(ObjectNode requestJson) {
        return serialize(requestJson).flatMap(this::request).flatMap(this::envelope);
    }

    private Result<HttpResponse<String>> request(String body) {
        var request = HttpRequest.newBuilder(cgiUri)
          .timeout(Duration.ofSeconds(15))
          .header("Content-Type", contentType)
          .header("X-Requested-With", requestedWith)
          .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
          .build();
        return send(request);
    }

    private Result<JsonNode> envelope(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            return new Result.Failure<>(new RouterFault.TransportFault("Unexpected HTTP status: " + response.statusCode()));
        }
        return trimToJsonObject(response.body()).flatMap(this::parse).flatMap(this::validate);
    }

    private Result<JsonNode> validate(JsonNode json) {
        if (!json.isObject()) {
            return new Result.Failure<>(new RouterFault.MalformedResponseFault("Router response is not a JSON object"));
        }
        if (json.has("success") && !json.path("success").asBoolean()) {
            var message = json.path("message").asText("Router returned success=false");
            return new Result.Failure<>(faultForEnvelope(message));
        }
        return new Result.Success<>(json);
    }

    private Result<String> serialize(ObjectNode requestJson) {
        try {
            return new Result.Success<>(mapper.writeValueAsString(requestJson));
        } catch (JsonProcessingException e) {
            return new Result.Failure<>(new RouterFault.ProtocolFault("Failed to serialize request JSON"));
        }
    }

    private Result<HttpResponse<String>> send(HttpRequest request) {
        try {
            return new Result.Success<>(httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)));
        } catch (HttpTimeoutException e) {
            return new Result.Failure<>(new RouterFault.TimeoutFault("HTTP request timed out"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Result.Failure<>(new RouterFault.TimeoutFault("HTTP request interrupted"));
        } catch (IOException e) {
            return new Result.Failure<>(new RouterFault.TransportFault("HTTP request failed"));
        }
    }

    private Result<JsonNode> parse(String jsonText) {
        try {
            return new Result.Success<>(mapper.readTree(jsonText));
        } catch (JsonProcessingException e) {
            return new Result.Failure<>(new RouterFault.MalformedResponseFault("Failed to parse response JSON"));
        }
    }

    private Result<String> trimToJsonObject(String raw) {
        if (raw == null) {
            return new Result.Failure<>(new RouterFault.MalformedResponseFault("Empty response body"));
        }
        var idx = raw.indexOf('{');
        if (idx < 0) {
            return new Result.Failure<>(new RouterFault.MalformedResponseFault("Response does not contain a JSON object"));
        }
        return new Result.Success<>(raw.substring(idx).trim());
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

    private RouterFault faultForEnvelope(String message) {
        var detail = message == null ? "" : message;
        var lower = detail.toLowerCase(Locale.ROOT);
        if (lower.contains("unsupported")) {
            return new RouterFault.UnsupportedCommandFault(detail);
        }
        if (lower.contains("session") || lower.contains("expire")) {
            return new RouterFault.SessionExpiredFault(detail);
        }
        if (lower.contains("auth") || lower.contains("login") || lower.contains("password")) {
            return new RouterFault.AuthFault(detail);
        }
        return new RouterFault.ProtocolFault(detail);
    }
}
