package com.github.idelstak.routerfx.dashboard.network;

import java.util.*;

final class ActiveLoop implements RefreshLoop {

    private final LoopTask task;
    private final Thread thread;

    ActiveLoop(LoopTask task, Thread thread) {
        this.task = Objects.requireNonNull(task, "task must not be null");
        this.thread = Objects.requireNonNull(thread, "thread must not be null");
    }

    @Override
    public void cancel() {
        task.stop();
        thread.interrupt();
    }

    @Override
    public boolean active() {
        return task.live() && thread.isAlive();
    }
}
