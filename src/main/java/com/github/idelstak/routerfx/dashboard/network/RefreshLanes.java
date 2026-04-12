package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;
import java.util.*;

final class RefreshLanes {

    private final RefreshLoop common;
    private final RefreshLoop auth;

    RefreshLanes(RefreshLoop common, RefreshLoop auth) {
        this.common = Objects.requireNonNull(common, "common must not be null");
        this.auth = Objects.requireNonNull(auth, "auth must not be null");
    }

    RefreshLanes common(LoopStart start, Duration wait, Runnable tick) {
        var nextCommon = common.active() ? common : start.begin(wait, tick);
        var nextAuth = idle(auth);
        return new RefreshLanes(nextCommon, nextAuth);
    }

    RefreshLanes auth(LoopStart start, Duration wait, Runnable tick) {
        var nextAuth = auth.active() ? auth : start.begin(wait, tick);
        var nextCommon = idle(common);
        return new RefreshLanes(nextCommon, nextAuth);
    }

    private RefreshLoop idle(RefreshLoop loop) {
        if (loop.active()) {
            loop.cancel();
        }
        return new NoopLoop();
    }
}
