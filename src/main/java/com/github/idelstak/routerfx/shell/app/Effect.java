package com.github.idelstak.routerfx.shell.app;

import java.util.*;
import java.util.function.*;

public interface Effect {

    Optional<Msg> apply(AppState state, Msg msg);

    default void attach(Consumer<Msg> dispatch) {
    }
}
