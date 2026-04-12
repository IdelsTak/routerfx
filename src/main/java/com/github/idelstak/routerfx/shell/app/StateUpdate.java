package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.auth.login.*;
import com.github.idelstak.routerfx.dashboard.network.*;
import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;

public final class StateUpdate implements Update {

    @Override
    public AppState apply(AppState state, Msg msg) {
        return switch (msg) {
            case Msg.ConnectRequested(var baseUrl, var credentials) ->
                connect(state, baseUrl, credentials);
            case Msg.RefreshRequested _ ->
                refresh(state);
            case Msg.Authenticated(var session, var radio) ->
                authenticate(state, session, radio);
            case Msg.DashboardLoaded(var radio) ->
                load(state, radio);
            case Msg.Failed(var fault) ->
                fail(state, fault);
        };
    }

    private AppState connect(AppState state, String baseUrl, Credentials credentials) {
        return new AppState(
          new LoginState(baseUrl, credentials.username(), Optional.empty(), Optional.empty()),
          new DashboardState(Optional.empty(), Optional.empty(), false, state.dashboard().updates()),
          new UiState(true, false, "Connecting", false)
        );
    }

    private AppState refresh(AppState state) {
        return new AppState(
          state.login(),
          new DashboardState(state.dashboard().radio(), Optional.empty(), true, state.dashboard().updates()),
          new UiState(true, state.login().session().isPresent(), "Refreshing", state.login().session().isPresent())
        );
    }

    private AppState authenticate(AppState state, Session session, RadioState radio) {
        return new AppState(
          new LoginState(state.login().baseUrl(), state.login().username(), Optional.of(session), Optional.empty()),
          new DashboardState(Optional.of(radio), Optional.empty(), false, state.dashboard().updates() + 1),
          new UiState(false, true, "Connected", true)
        );
    }

    private AppState load(AppState state, RadioState radio) {
        return new AppState(
          state.login(),
          new DashboardState(Optional.of(radio), Optional.empty(), false, state.dashboard().updates() + 1),
          new UiState(false, state.login().session().isPresent(), "Updated", state.login().session().isPresent())
        );
    }

    private AppState fail(AppState state, RouterFault fault) {
        return new AppState(
          new LoginState(
            state.login().baseUrl(),
            state.login().username(),
            session(state.login().session(), fault),
            Optional.of(fault)
          ),
          new DashboardState(state.dashboard().radio(), Optional.of(fault), false, state.dashboard().updates()),
          new UiState(false, state.login().session().isPresent(), "Failed", state.login().session().isPresent())
        );
    }

    private Optional<Session> session(Optional<Session> session, RouterFault fault) {
        return switch (fault) {
            case RouterFault.AuthFault _, RouterFault.SessionExpiredFault _ ->
                Optional.empty();
            case RouterFault.TimeoutFault _, RouterFault.TransportFault _, RouterFault.ProtocolFault _, RouterFault.MalformedResponseFault _, RouterFault.UnsupportedCommandFault _ ->
                session;
        };
    }
}
