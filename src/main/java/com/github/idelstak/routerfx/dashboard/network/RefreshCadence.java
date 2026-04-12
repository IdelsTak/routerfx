package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;
import java.util.*;

record RefreshCadence(Duration common, Duration auth) {

    RefreshCadence {
        Objects.requireNonNull(common, "common must not be null");
        Objects.requireNonNull(auth, "auth must not be null");
        if (common.isZero() || common.isNegative()) {
            throw new IllegalArgumentException("common must be greater than zero");
        }
        if (auth.isZero() || auth.isNegative()) {
            throw new IllegalArgumentException("auth must be greater than zero");
        }
    }
}
