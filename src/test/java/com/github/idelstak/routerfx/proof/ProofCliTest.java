package com.github.idelstak.routerfx.proof;

import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class ProofCliTest {

    @Test
    void returnsUsageCodeWhenPasswordIsPassedInArgs() {
        var outBytes = new ByteArrayOutputStream();
        var errBytes = new ByteArrayOutputStream();
        var cli = new ProofCli(
          (username, err) -> "ignored".toCharArray(),
          baseUrl -> new RouterApi() {
            @Override
            public Challenge fetchChallenge() {
                throw new AssertionError("Expected fetchChallenge not to run for invalid args");
            }

            @Override
            public Session login(Credentials credentials, Challenge challenge) {
                throw new AssertionError("Expected login not to run for invalid args");
            }

            @Override
            public RadioState fetchRadioState(Session session) {
                throw new AssertionError("Expected fetchRadioState not to run for invalid args");
            }
        },
          new PrintStream(outBytes, true, StandardCharsets.UTF_8),
          new PrintStream(errBytes, true, StandardCharsets.UTF_8)
        );
        var code = cli.run(new String[]{"http://192.168.1.1", "admin", "bad"});
        assertThat("Expected usage code when password is passed in arguments", code, is(2));
    }

    @Test
    void doesNotCreateRouterApiWhenPasswordIsPassedInArgs() {
        var outBytes = new ByteArrayOutputStream();
        var errBytes = new ByteArrayOutputStream();
        var created = new AtomicBoolean(false);
        var cli = new ProofCli(
          (username, err) -> "ignored".toCharArray(),
          baseUrl -> {
            created.set(true);
            return new RouterApi() {
                @Override
                public Challenge fetchChallenge() {
                    throw new AssertionError("Expected no API calls for invalid args");
                }

                @Override
                public Session login(Credentials credentials, Challenge challenge) {
                    throw new AssertionError("Expected no API calls for invalid args");
                }

                @Override
                public RadioState fetchRadioState(Session session) {
                    throw new AssertionError("Expected no API calls for invalid args");
                }
            };
        },
          new PrintStream(outBytes, true, StandardCharsets.UTF_8),
          new PrintStream(errBytes, true, StandardCharsets.UTF_8)
        );
        cli.run(new String[]{"http://192.168.1.1", "admin", "bad"});
        assertThat("Expected API factory to stay unused for invalid args", created.get(), is(false));
    }

    @Test
    void forwardsSecureInputPasswordToRouterApi() {
        var outBytes = new ByteArrayOutputStream();
        var errBytes = new ByteArrayOutputStream();
        var seenPassword = new AtomicReference<>("");
        var cli = new ProofCli(
          (username, err) -> "s3cr3t".toCharArray(),
          baseUrl -> new RouterApi() {
            @Override
            public Challenge fetchChallenge() {
                return new Challenge("token");
            }

            @Override
            public Session login(Credentials credentials, Challenge challenge) {
                seenPassword.set(credentials.password());
                return new Session("session");
            }

            @Override
            public RadioState fetchRadioState(Session session) {
                return new RadioState("Airtel", "LTE", "-91", "-61", "-10", "21", "B3", "20M", "123", "45", "00:10:00", "600");
            }
        },
          new PrintStream(outBytes, true, StandardCharsets.UTF_8),
          new PrintStream(errBytes, true, StandardCharsets.UTF_8)
        );
        cli.run(new String[]{"http://192.168.1.1", "admin"});
        assertThat("Expected secure password to be forwarded to API login call", seenPassword.get(), is("s3cr3t"));
    }

    @Test
    void clearsMutablePasswordBufferAfterRun() {
        var outBytes = new ByteArrayOutputStream();
        var errBytes = new ByteArrayOutputStream();
        var provided = "s3cr3t".toCharArray();
        var cli = new ProofCli(
          (username, err) -> provided,
          baseUrl -> new RouterApi() {
            @Override
            public Challenge fetchChallenge() {
                return new Challenge("token");
            }

            @Override
            public Session login(Credentials credentials, Challenge challenge) {
                return new Session("session");
            }

            @Override
            public RadioState fetchRadioState(Session session) {
                return new RadioState("Airtel", "LTE", "-91", "-61", "-10", "21", "B3", "20M", "123", "45", "00:10:00", "600");
            }
        },
          new PrintStream(outBytes, true, StandardCharsets.UTF_8),
          new PrintStream(errBytes, true, StandardCharsets.UTF_8)
        );
        cli.run(new String[]{"http://192.168.1.1", "admin"});
        var allCleared = new AtomicBoolean(true);
        for (var item : provided) {
            if (item != '\0') {
                allCleared.set(false);
            }
        }
        assertThat("Expected mutable password buffer to be cleared after run", allCleared.get(), is(true));
    }

    @Test
    void doesNotPrintPasswordWhenLoginFails() {
        var outBytes = new ByteArrayOutputStream();
        var errBytes = new ByteArrayOutputStream();
        var secret = "s3cr3t";
        var cli = new ProofCli(
          (username, err) -> secret.toCharArray(),
          baseUrl -> new RouterApi() {
            @Override
            public Challenge fetchChallenge() {
                return new Challenge("token");
            }

            @Override
            public Session login(Credentials credentials, Challenge challenge) {
                throw new RouterProtocolException("Login failed for password " + credentials.password());
            }

            @Override
            public RadioState fetchRadioState(Session session) {
                throw new AssertionError("Expected no radio fetch after login failure");
            }
        },
          new PrintStream(outBytes, true, StandardCharsets.UTF_8),
          new PrintStream(errBytes, true, StandardCharsets.UTF_8)
        );
        cli.run(new String[]{"http://192.168.1.1", "admin"});
        var errText = errBytes.toString(StandardCharsets.UTF_8);
        assertThat("Expected secret to stay out of error output", errText, not(containsString(secret)));
    }
}
