package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class StoreTest {

    @Test
    void dispatchChangesStateThroughUpdateContract() {
        var update = new StateUpdate();
        Effect effect = (AppState _, Msg _) -> Optional.empty();
        var store = new Store(AppState.initial(), update, effect, Runnable::run);
        store.dispatch(new Msg.ConnectRequested("http://192.168.1.1", new Credentials("admin", "päss")));
        assertThat("Expected store dispatch to mutate state through update", store.read().ui().busy(), is(true));
    }

    @Test
    void dispatchFeedsEffectMessageBackIntoStore() {
        var update = new StateUpdate();
        Effect effect = (AppState _, Msg msg) -> switch (msg) {
            case Msg.ConnectRequested _ ->
                Optional.of(new Msg.Authenticated(new Session("sess-x"), radio()));
            case Msg.RefreshRequested _, Msg.Authenticated _, Msg.DashboardLoaded _, Msg.CommonLoaded _, Msg.Failed _ ->
                Optional.empty();
        };
        var store = new Store(AppState.initial(), update, effect, Runnable::run);
        store.dispatch(new Msg.ConnectRequested("http://192.168.1.1", new Credentials("admin", "päss")));
        assertThat("Expected store to apply message emitted by effect", store.read().ui().connected(), is(true));
    }

    @Test
    void dispatchKeepsFailureMappingWithinMessageFlow() {
        var update = new StateUpdate();
        Effect effect = (AppState _, Msg msg) -> switch (msg) {
            case Msg.RefreshRequested _ ->
                Optional.of(new Msg.Failed(new RouterFault.TimeoutFault("slow")));
            case Msg.ConnectRequested _, Msg.Authenticated _, Msg.DashboardLoaded _, Msg.CommonLoaded _, Msg.Failed _ ->
                Optional.empty();
        };
        var authenticated = update.apply(AppState.initial(), new Msg.Authenticated(new Session("sess-y"), radio()));
        var store = new Store(authenticated, update, effect, Runnable::run);
        store.dispatch(new Msg.RefreshRequested());
        assertThat("Expected timeout failure to stay in state via failed message", store.read().dashboard().fault().isPresent(), is(true));
    }

    private RadioState radio() {
        return new RadioState("Åirtel", "LTE", "-88", "-58", "-9", "24", "B28", "20M", "33", "8", "00:05:00", "300");
    }
}
