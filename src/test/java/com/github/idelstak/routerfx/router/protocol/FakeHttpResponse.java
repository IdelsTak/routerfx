package com.github.idelstak.routerfx.router.protocol;

import java.net.*;
import java.net.http.*;
import java.util.*;
import javax.net.ssl.*;

final class FakeHttpResponse implements HttpResponse<String> {

    private final int status;
    private final String payload;
    private final HttpRequest request;

    FakeHttpResponse(int status, String payload, HttpRequest request) {
        this.status = status;
        this.payload = payload;
        this.request = request;
    }

    @Override
    public int statusCode() {
        return this.status;
    }

    @Override
    public HttpRequest request() {
        return this.request;
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(Map.of(), (a, b) -> true);
    }

    @Override
    public String body() {
        return this.payload;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return this.request.uri();
    }

    @Override
    public HttpClient.Version version() {
        return HttpClient.Version.HTTP_1_1;
    }
}
