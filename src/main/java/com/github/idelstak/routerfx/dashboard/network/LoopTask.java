package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;

final class LoopTask implements Runnable {

    private final Duration wait;
    private final Runnable tick;
    private final AtomicBoolean live;

    LoopTask(Duration wait, Runnable tick) {
        this.wait = Objects.requireNonNull(wait, "wait must not be null");
        this.tick = Objects.requireNonNull(tick, "tick must not be null");
        live = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        while (live.get()) {
            try {
                Thread.sleep(wait);
                if (live.get()) {
                    tick.run();
                }
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                live.set(false);
            }
        }
    }

    void stop() {
        live.set(false);
    }

    boolean live() {
        return live.get();
    }
}
