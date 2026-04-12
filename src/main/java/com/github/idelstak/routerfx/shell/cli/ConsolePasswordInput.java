package com.github.idelstak.routerfx.shell.cli;

import com.github.idelstak.routerfx.router.protocol.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public final class ConsolePasswordInput implements PasswordInput {

    private final InputStream input;

    public ConsolePasswordInput(InputStream input) {
        this.input = Objects.requireNonNull(input, "input must not be null");
    }

    @Override
    public char[] read(String username, PrintStream err) {
        var console = System.console();
        if (console != null) {
            var secret = console.readPassword("Password for %s: ", username);
            if (secret != null) {
                return secret;
            }
        }
        err.print("Password: ");
        err.flush();
        try {
            var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            var line = reader.readLine();
            if (line == null) {
                return new char[0];
            }
            return line.toCharArray();
        } catch (IOException e) {
            throw new RouterProtocolException("Failed to read password from standard input", e);
        }
    }
}
