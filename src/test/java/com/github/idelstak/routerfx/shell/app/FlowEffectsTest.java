package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class FlowEffectsTest {

    @Test
    void connectRequestedEmitsAuthenticatedWhenBoundaryCallsSucceed() {
        var effect = new FlowEffects(baseUrl -> okApi());
        var msg = effect.apply(
          AppState.initial(),
          new Msg.ConnectRequested("http://router.local", new Credentials("admin", "päss"))
        ).orElseThrow();
        assertThat("Expected connect effect to emit authenticated message on success", msg, instanceOf(Msg.Authenticated.class));
    }

    @Test
    void connectRequestedEmitsFailedWhenBoundaryLoginFails() {
        var effect = new FlowEffects(baseUrl -> authFailApi());
        var msg = effect.apply(
          AppState.initial(),
          new Msg.ConnectRequested("http://router.local", new Credentials("admin", "päss"))
        ).orElseThrow();
        assertThat("Expected connect effect to emit failed message on auth fault", msg, instanceOf(Msg.Failed.class));
    }

    @Test
    void refreshRequestedEmitsFailedWhenSessionIsMissing() {
        var effect = new FlowEffects(baseUrl -> okApi());
        var msg = effect.apply(AppState.initial(), new Msg.RefreshRequested()).orElseThrow();
        assertThat("Expected refresh effect to fail when no session exists", msg, instanceOf(Msg.Failed.class));
    }

    @Test
    void refreshRequestedEmitsDashboardLoadedWhenSessionExists() {
        var effect = new FlowEffects(baseUrl -> okApi());
        var state = new StateUpdate().apply(AppState.initial(), new Msg.Authenticated(new Session("sess-1"), radio()));
        var msg = effect.apply(state, new Msg.RefreshRequested()).orElseThrow();
        assertThat("Expected refresh effect to emit dashboard-loaded message", msg, instanceOf(Msg.DashboardLoaded.class));
    }

    private RouterApi okApi() {
        return new RouterApi() {
            @Override
            public Result<Challenge> fetchChallenge() {
                return new Result.Success<>(new Challenge("tok-1"));
            }

            @Override
            public Result<Session> login(Credentials credentials, Challenge challenge) {
                return new Result.Success<>(new Session("sess-1"));
            }

            @Override
            public Result<RadioState> fetchRadioState(Session session) {
                return new Result.Success<>(radio());
            }
        };
    }

    private RouterApi authFailApi() {
        return new RouterApi() {
            @Override
            public Result<Challenge> fetchChallenge() {
                return new Result.Success<>(new Challenge("tok-1"));
            }

            @Override
            public Result<Session> login(Credentials credentials, Challenge challenge) {
                return new Result.Failure<>(new RouterFault.AuthFault("invalid"));
            }

            @Override
            public Result<RadioState> fetchRadioState(Session session) {
                return new Result.Success<>(radio());
            }
        };
    }

    private RadioState radio() {
        return new RadioState("Åirtel", "LTE", "-90", "-65", "-10", "21", "B3", "20M", "20", "5", "00:03:00", "180");
    }
}
