package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;
import java.util.*;

final class ManualLoopStart implements LoopStart {

    private final List<ManualLoop> loops;

    ManualLoopStart() {
        loops = new ArrayList<>();
    }

    @Override
    public RefreshLoop begin(Duration interval, Runnable command) {
        var loop = new ManualLoop(interval, command);
        loops.add(loop);
        return loop;
    }

    long activeCount(Duration interval) {
        return loops.stream()
          .filter(loop -> loop.interval().equals(interval))
          .filter(ManualLoop::active)
          .count();
    }

    String activeDescription() {
        return "9:%d,4:%d".formatted(activeCount(Duration.ofSeconds(9)), activeCount(Duration.ofSeconds(4)));
    }

    void tick(Duration interval) {
        loops.stream()
          .filter(loop -> loop.interval().equals(interval))
          .filter(ManualLoop::active)
          .findFirst()
          .ifPresent(ManualLoop::tick);
    }
}
