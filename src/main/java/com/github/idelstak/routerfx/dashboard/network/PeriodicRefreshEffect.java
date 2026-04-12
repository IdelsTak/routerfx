package com.github.idelstak.routerfx.dashboard.network;

import com.github.idelstak.routerfx.shell.app.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

public final class PeriodicRefreshEffect implements Effect {

    private final LoopStart start;
    private final RefreshCadence cadence;
    private Consumer<Msg> dispatch;
    private RefreshLanes lanes;

    public PeriodicRefreshEffect() {
        this(new VirtualLoopStart(), new RefreshCadence(Duration.ofSeconds(6), Duration.ofSeconds(4)));
    }

    PeriodicRefreshEffect(LoopStart start, RefreshCadence cadence) {
        this.start = Objects.requireNonNull(start, "start must not be null");
        this.cadence = Objects.requireNonNull(cadence, "cadence must not be null");
        dispatch = msg -> {
        };
        lanes = new RefreshLanes(new NoopLoop(), new NoopLoop());
    }

    @Override
    public synchronized Optional<Msg> apply(AppState state, Msg msg) {
        lanes = state.login().session().isPresent()
          ? lanes.auth(start, cadence.auth(), this::tick)
          : lanes.common(start, cadence.common(), this::tick);
        return Optional.empty();
    }

    @Override
    public synchronized void attach(Consumer<Msg> dispatch) {
        this.dispatch = Objects.requireNonNull(dispatch, "dispatch must not be null");
    }

    private void tick() {
        dispatch.accept(new Msg.RefreshRequested());
    }
}
