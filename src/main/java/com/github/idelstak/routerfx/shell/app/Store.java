package com.github.idelstak.routerfx.shell.app;

import java.util.*;
import java.util.concurrent.*;

public final class Store {

    private final Update update;
    private final Effect effect;
    private final Executor executor;
    private AppState state;

    public Store(AppState state, Update update, Effect effect, Executor executor) {
        this.state = Objects.requireNonNull(state, "state must not be null");
        this.update = Objects.requireNonNull(update, "update must not be null");
        this.effect = Objects.requireNonNull(effect, "effect must not be null");
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
    }

    public synchronized AppState read() {
        return state;
    }

    public void dispatch(Msg msg) {
        Objects.requireNonNull(msg, "msg must not be null");
        var snapshot = mutate(msg);
        executor.execute(() -> effect.apply(snapshot, msg).ifPresent(this::dispatch));
    }

    private synchronized AppState mutate(Msg msg) {
        state = update.apply(state, msg);
        return state;
    }
}
