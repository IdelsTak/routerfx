package com.github.idelstak.routerfx.shell.app;

import java.util.*;

public interface Effect {

    Optional<Msg> apply(AppState state, Msg msg);
}
