package com.github.idelstak.routerfx.shell.cli;

import java.io.*;

public interface PasswordInput {

    char[] read(String username, PrintStream err);
}
