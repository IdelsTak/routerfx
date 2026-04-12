package com.github.idelstak.routerfx.shell.cli;

import com.github.idelstak.routerfx.router.protocol.*;
import java.io.*;
import java.nio.charset.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class ConsolePasswordInputTest {

    @Test
    void readsPasswordFromStandardInputFallback() {
        var input = new ByteArrayInputStream("s3cr3t\n".getBytes(StandardCharsets.UTF_8));
        var errBytes = new ByteArrayOutputStream();
        var reader = new ConsolePasswordInput(input);
        var secret = reader.read("admin", new PrintStream(errBytes, true, StandardCharsets.UTF_8));
        assertThat("Expected password chars from stdin fallback", new String(secret), is("s3cr3t"));
    }

    @Test
    void returnsEmptyArrayWhenStandardInputHasNoLine() {
        var input = new ByteArrayInputStream(new byte[0]);
        var errBytes = new ByteArrayOutputStream();
        var reader = new ConsolePasswordInput(input);
        var secret = reader.read("admin", new PrintStream(errBytes, true, StandardCharsets.UTF_8));
        assertThat("Expected empty chars when stdin is exhausted", secret.length, is(0));
    }

    @Test
    void wrapsIoExceptionsFromInputStream() {
        var input = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };
        var errBytes = new ByteArrayOutputStream();
        var reader = new ConsolePasswordInput(input);
        var thrown = Assertions.assertThrows(
          RouterProtocolException.class,
          () -> reader.read("admin", new PrintStream(errBytes, true, StandardCharsets.UTF_8))
        );
        assertThat("Expected read failure context in exception", thrown.getMessage(), containsString("Failed to read password"));
    }
}
