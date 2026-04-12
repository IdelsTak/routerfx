package com.github.idelstak.routerfx.dashboard.network;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shell.app.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class PeriodicRefreshEffectTest {

    @Test
    void refreshRequestedStartsCommonRefreshLoopForPreLoginState() {
        var start = new ManualLoopStart();
        var effect = new PeriodicRefreshEffect(start, new RefreshCadence(Duration.ofSeconds(9), Duration.ofSeconds(4)));
        effect.attach(msg -> {
        });
        effect.apply(AppState.initial(), new Msg.RefreshRequested());
        assertThat("Expected pre-login state to start common periodic loop", start.activeCount(Duration.ofSeconds(9)), is(1L));
    }

    @Test
    void authenticatedMessageSwitchesFromCommonToAuthenticatedLoop() {
        var start = new ManualLoopStart();
        var effect = new PeriodicRefreshEffect(start, new RefreshCadence(Duration.ofSeconds(9), Duration.ofSeconds(4)));
        effect.attach(msg -> {
        });
        effect.apply(AppState.initial(), new Msg.RefreshRequested());
        var authenticatedState = new StateUpdate().apply(AppState.initial(), new Msg.Authenticated(new Session("sess-1"), radio()));
        effect.apply(authenticatedState, new Msg.Authenticated(new Session("sess-1"), radio()));
        assertThat("Expected authenticated state to run only authenticated loop", start.activeDescription(), is("9:0,4:1"));
    }

    @Test
    void sessionExpirySwitchesBackToCommonLoop() {
        var start = new ManualLoopStart();
        var effect = new PeriodicRefreshEffect(start, new RefreshCadence(Duration.ofSeconds(9), Duration.ofSeconds(4)));
        effect.attach(msg -> {
        });
        var update = new StateUpdate();
        var authenticated = update.apply(AppState.initial(), new Msg.Authenticated(new Session("sess-1"), radio()));
        effect.apply(authenticated, new Msg.Authenticated(new Session("sess-1"), radio()));
        var recovered = update.apply(authenticated, new Msg.Failed(new RouterFault.SessionExpiredFault("expired")));
        effect.apply(recovered, new Msg.Failed(new RouterFault.SessionExpiredFault("expired")));
        assertThat("Expected session-expiry transition to stop authenticated loop and resume common loop", start.activeDescription(), is("9:1,4:0"));
    }

    @Test
    void activeLoopTickDispatchesRefreshRequested() {
        var start = new ManualLoopStart();
        var effect = new PeriodicRefreshEffect(start, new RefreshCadence(Duration.ofSeconds(9), Duration.ofSeconds(4)));
        var dispatched = new ArrayList<Msg>();
        effect.attach(dispatched::add);
        effect.apply(AppState.initial(), new Msg.RefreshRequested());
        start.tick(Duration.ofSeconds(9));
        assertThat("Expected periodic tick to dispatch refresh request", dispatched.getFirst(), instanceOf(Msg.RefreshRequested.class));
    }

    private RadioState radio() {
        return new RadioState("Åirtel", "LTE", "-85", "-62", "-9", "20", "B3", "20M", "1", "1", "00:01:00", "12");
    }
}
