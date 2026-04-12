package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;

final class VirtualLoopStart implements LoopStart {

    @Override
    public RefreshLoop begin(Duration wait, Runnable tick) {
        var loop = new LoopTask(wait, tick);
        var thread = Thread.ofVirtual()
          .name("routerfx-refresh-loop")
          .start(loop);
        return new ActiveLoop(loop, thread);
    }
}
