package com.github.idelstak.routerfx.proof;

import java.io.*;

public interface PasswordInput {
    char[] read(String username, PrintStream err);
}
