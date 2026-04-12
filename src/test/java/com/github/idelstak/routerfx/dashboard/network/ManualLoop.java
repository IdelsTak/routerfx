package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;
import java.util.*;

final class ManualLoop implements RefreshLoop {

    private final Duration interval;
    private final Runnable command;
    private boolean active;

    ManualLoop(Duration interval, Runnable command) {
        this.interval = Objects.requireNonNull(interval, "interval must not be null");
        this.command = Objects.requireNonNull(command, "command must not be null");
        active = true;
    }

    Duration interval() {
        return interval;
    }

    void tick() {
        command.run();
    }

    @Override
    public void cancel() {
        active = false;
    }

    @Override
    public boolean active() {
        return active;
    }
}
