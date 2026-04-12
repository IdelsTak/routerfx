package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.databind.*;
import com.github.idelstak.routerfx.shared.result.*;
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
        assertThat("Expected token from challenge response", value(result).token(), is("abc123"));
    }

    @Test
    void fetchChallengeReturnsFailureWhenRouterReturnsSuccessFalse() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":232,\"success\":false,\"message\":\"NO_AUTH\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected auth fault when challenge is rejected", fault(result), instanceOf(RouterFault.AuthFault.class));
    }

    @Test
    void fetchChallengeReturnsSessionExpiredFaultWhenEnvelopeReportsExpiredSession() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":232,\"success\":false,\"message\":\"session expired\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected session-expired fault when envelope reports expired session", fault(result), instanceOf(RouterFault.SessionExpiredFault.class));
    }

    @Test
    void fetchChallengeReturnsUnsupportedCommandFaultWhenEnvelopeReportsUnsupportedCommand() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":232,\"success\":false,\"message\":\"unsupported command\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected unsupported-command fault when envelope reports unsupported command", fault(result), instanceOf(RouterFault.UnsupportedCommandFault.class));
    }

    @Test
    void loginReturnsSessionIdWhenRouterAcceptsCredentials() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":100,\"success\":true,\"sessionId\":\"sess-001\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.login(new Credentials("admin", "pw"), new Challenge("tok"));
        assertThat("Expected authenticated session id", value(result).sessionId(), is("sess-001"));
    }

    @Test
    void loginReturnsFailureWhenRouterRejectsCredentials() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "{\"cmd\":100,\"success\":true,\"login_fail\":\"fail\"}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.login(new Credentials("admin", "pw"), new Challenge("tok"));
        assertThat("Expected auth fault for rejected login", fault(result), instanceOf(RouterFault.AuthFault.class));
    }

    @Test
    void fetchRadioStateReturnsOperatorField() {
        var payload = "{\"cmd\":205,\"success\":true,\"network_operator\":\"Airtel\",\"network_type_str\":\"LTE\",\"RSRP\":\"-90\",\"RSSI\":\"-60\",\"RSRQ\":\"-11\",\"SINR\":\"20\",\"currentband\":\"B3\",\"bandwidth\":\"20M\",\"flow_dl\":\"100\",\"flow_ul\":\"20\",\"onlineTime\":\"01:00:00\",\"onlineDuration\":\"3600\"}";
        var client = new FakeHttpClient(request -> new FakeHttpResponse(200, payload, request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchRadioState(new Session("sess"));
        assertThat("Expected operator from radio response", value(result).networkOperator(), is("Airtel"));
    }

    @Test
    void fetchRadioStateReturnsFailureWhenPayloadIsMalformed() {
        var payload = "{\"cmd\":205,\"success\":true,\"network_type_str\":\"LTE\"}";
        var client = new FakeHttpClient(request -> new FakeHttpResponse(200, payload, request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchRadioState(new Session("sess"));
        assertThat("Expected malformed-response fault when required radio fields are missing", fault(result), instanceOf(RouterFault.MalformedResponseFault.class));
    }

    @Test
    void fetchChallengeReturnsFailureWhenHttpStatusIsNotOk() {
        var client = new FakeHttpClient(request -> new FakeHttpResponse(500, "{}", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected transport fault for non-200 response", fault(result), instanceOf(RouterFault.TransportFault.class));
    }

    @Test
    void fetchChallengeReturnsTimeoutFaultWhenHttpClientTimesOut() {
        var client = new TimeoutHttpClient();
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected timeout fault when HTTP client times out", fault(result), instanceOf(RouterFault.TimeoutFault.class));
    }

    @Test
    void fetchChallengeReturnsFailureWhenResponseHasNoJsonObject() {
        var client = new FakeHttpClient(request ->
          new FakeHttpResponse(200, "plain-text-response", request));
        var api = new HttpRouterApi("http://router.local", "en", client, new ObjectMapper());
        var result = api.fetchChallenge();
        assertThat("Expected malformed-response fault for non-json body", fault(result), instanceOf(RouterFault.MalformedResponseFault.class));
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
        var session = challenge.flatMap(token -> api.login(new Credentials("admin", "pw"), token));
        var radio = session.flatMap(api::fetchRadioState);
        assertThat("Expected core flow to return normalized network type", value(radio).networkTypeStr(), is("LTE"));
    }

    private <T> T value(Result<T> result) {
        return result.fold(value -> value, fault -> {
            throw new AssertionError("Expected success but got fault: " + fault);
        });
    }

    private RouterFault fault(Result<?> result) {
        return result.fold(value -> {
            throw new AssertionError("Expected fault but got success value");
        }, item -> item);
    }
}
