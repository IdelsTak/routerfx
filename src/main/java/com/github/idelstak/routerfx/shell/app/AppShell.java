package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import java.util.*;
import java.util.concurrent.*;

public final class AppShell {

    private final Store store;

    public AppShell(RouterApiFactory apiFactory, Executor executor) {
        Objects.requireNonNull(apiFactory, "apiFactory must not be null");
        Objects.requireNonNull(executor, "executor must not be null");
        store = new Store(
          AppState.initial(),
          new StateUpdate(),
          new FlowEffects(apiFactory),
          executor
        );
    }

    public Store store() {
        return store;
    }
}
