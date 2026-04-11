package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.databind.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class HttpRouterApiTest {

    @Test
    void fetchChallengeReturnsTokenFromRouterResponse() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":232,\"success\":true,\"token\":\"abc123\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected token from challenge response", result.token(), is("abc123"));
    }

    @Test
    void loginReturnsSessionIdWhenRouterAcceptsCredentials() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":100,\"success\":true,\"sessionId\":\"sess-001\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var session = api.login(new Credentials("admin", "pw"), new Challenge("tok"));
        assertThat("Expected authenticated session id", session.sessionId(), is("sess-001"));
    }

    @Test
    void loginThrowsWhenRouterReturnsLoginFailFlag() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":100,\"success\":true,\"login_fail\":\"fail\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var thrown = Assertions.assertThrows(RouterProtocolException.class, () ->
          api.login(new Credentials("admin", "pw"), new Challenge("tok")));
        assertThat("Expected explicit login rejection error", thrown.getMessage(), containsString("Login rejected"));
    }

    @Test
    void fetchRadioStateReturnsOperatorField() {
        var payload = "{\"cmd\":205,\"success\":true,\"network_operator\":\"Airtel\",\"network_type_str\":\"LTE\",\"RSRP\":\"-90\",\"RSSI\":\"-60\",\"RSRQ\":\"-11\",\"SINR\":\"20\",\"currentband\":\"B3\",\"bandwidth\":\"20M\",\"flow_dl\":\"100\",\"flow_ul\":\"20\",\"onlineTime\":\"01:00:00\",\"onlineDuration\":\"3600\"}";
        var client = new FakeHttpClient(request -> new FakeHttpResponse(200, payload, request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var radio = api.fetchRadioState(new Session("sess"));
        assertThat("Expected operator from radio response", radio.networkOperator(), is("Airtel"));
    }

    @Test
    void fetchChallengeThrowsWhenRouterReturnsSuccessFalse() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":232,\"success\":false,\"message\":\"NO_AUTH\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var thrown = Assertions.assertThrows(RouterProtocolException.class, api::fetchChallenge);
        assertThat("Expected router error message in thrown exception", thrown.getMessage(), containsString("NO_AUTH"));
    }

    @Test
    void fetchChallengeThrowsWhenTokenFieldIsMissing() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":232,\"success\":true}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var thrown = Assertions.assertThrows(RouterProtocolException.class, api::fetchChallenge);
        assertThat("Expected missing token field error", thrown.getMessage(), containsString("Missing required field: token"));
    }

    @Test
    void fetchChallengeThrowsWhenHttpStatusIsNotOk() {
        var client = new FakeHttpClient(request -> new FakeHttpResponse(500, "{}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var thrown = Assertions.assertThrows(RouterProtocolException.class, api::fetchChallenge);
        assertThat("Expected non-200 status error", thrown.getMessage(), containsString("Unexpected HTTP status: 500"));
    }

    @Test
    void fetchChallengeThrowsWhenResponseHasNoJsonObject() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "plain-text-response", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var thrown = Assertions.assertThrows(RouterProtocolException.class, api::fetchChallenge);
        assertThat("Expected no-json-object parsing error", thrown.getMessage(), containsString("does not contain a JSON object"));
    }

    @Test
    void sendsExpectedHeaderForAjaxCompatibility() {
        var seen = new AtomicReference<>("");
        var client = new FakeHttpClient(request -> {
            seen.set(request.headers().firstValue("X-Requested-With").orElse(""));
            return new FakeHttpResponse(200, "{\"cmd\":232,\"success\":true,\"token\":\"abc\"}", request);
        });
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        api.fetchChallenge();
        assertThat("Expected ajax compatibility header", seen.get(), is("XMLHttpRequest"));
    }

    @Test
    void supportsChallengeLoginAndDashboardReadInSingleCoreFlow() {
        var index = new AtomicInteger(0);
        var client = new FakeHttpClient(request -> {
            var current = index.getAndIncrement();
            if (current == 0) {
                return new FakeHttpResponse(200, "{\"cmd\":232,\"success\":true,\"token\":\"tok-1\"}", request);
            }
            if (current == 1) {
                return new FakeHttpResponse(200, "{\"cmd\":100,\"success\":true,\"sessionId\":\"sess-1\"}", request);
            }
            return new FakeHttpResponse(
              200,
              "{\"cmd\":205,\"success\":true,\"network_operator\":\"Airtel\",\"network_type_str\":\"LTE\",\"RSRP\":\"-90\",\"RSSI\":\"-60\",\"RSRQ\":\"-11\",\"SINR\":\"20\",\"currentband\":\"B3\",\"bandwidth\":\"20M\",\"flow_dl\":\"100\",\"flow_ul\":\"20\",\"onlineTime\":\"01:00:00\",\"onlineDuration\":\"3600\"}",
              request
            );
        });
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var challenge = api.fetchChallenge();
        var session = api.login(new Credentials("admin", "pw"), challenge);
        var radio = api.fetchRadioState(session);
        assertThat("Expected core flow to return normalized network type", radio.networkTypeStr(), is("LTE"));
    }
}
