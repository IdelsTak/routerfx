package com.github.idelstak.routerfx.proof;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.*;
import java.nio.charset.*;
import org.junit.jupiter.api.*;

final class MainAndFactoryTest {
    @Test
    void mainRunReturnsCliResultForValidFlow() {
        var cli = new ProofCli(
                (username, err) -> "pw".toCharArray(),
                baseUrl -> new RouterApi() {
                    @Override
                    public Challenge fetchChallenge() {
                        return new Challenge("token");
                    }

                    @Override
                    public Session login(Credentials credentials, Challenge challenge) {
                        return new Session("sid");
                    }

                    @Override
                    public RadioState fetchRadioState(Session session) {
                        return new RadioState("Airtel", "LTE", "-91", "-61", "-10", "21", "B3", "20M", "123", "45", "00:10:00", "600");
                    }
                },
                new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8),
                new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8)
        );
        var app = new Main(cli);
        var code = app.run(new String[]{"http://127.0.0.1", "admin"});
        assertThat("Expected main wrapper to return delegated cli code", code, is(0));
    }

    @Test
    void defaultFactoryCreatesHttpRouterApiImplementation() {
        var factory = new DefaultRouterApiFactory();
        var api = factory.create("http://127.0.0.1");
        assertThat("Expected default factory to create HttpRouterApi", api, instanceOf(HttpRouterApi.class));
    }

    @Test
    void routerProtocolExceptionStoresCauseReference() {
        var cause = new IllegalStateException("broken");
        var error = new RouterProtocolException("failed", cause);
        assertThat("Expected constructor to preserve cause", error.getCause(), sameInstance(cause));
    }
}
