package com.github.idelstak.routerfx.shell.app;

import java.io.*;

public interface PasswordInput {

    char[] read(String username, PrintStream err);
}
