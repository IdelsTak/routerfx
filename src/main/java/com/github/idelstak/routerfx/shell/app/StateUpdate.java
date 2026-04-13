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
            case Msg.LoginOverlayOpened _ ->
                openLoginOverlay(state);
            case Msg.LoginOverlayClosed _ ->
                closeLoginOverlay(state);
            case Msg.ConnectRequested(var baseUrl, var credentials) ->
                connect(state, baseUrl, credentials);
            case Msg.RefreshRequested _ ->
                refresh(state);
            case Msg.Authenticated(var session, var radio, var statusBar) ->
                authenticate(state, session, radio, statusBar);
            case Msg.DashboardLoaded(var radio, var statusBar) ->
                load(state, radio, statusBar);
            case Msg.CommonLoaded(var common) ->
                common(state, common);
            case Msg.Failed(var fault) ->
                fail(state, fault);
        };
    }

    private AppState openLoginOverlay(AppState state) {
        return withUi(state, new UiState(
          state.ui().busy(),
          state.ui().connected(),
          state.ui().note(),
          state.ui().canRefresh(),
          true
        ));
    }

    private AppState closeLoginOverlay(AppState state) {
        return withUi(state, new UiState(
          state.ui().busy(),
          state.ui().connected(),
          state.ui().note(),
          state.ui().canRefresh(),
          false
        ));
    }

    private AppState connect(AppState state, String baseUrl, Credentials credentials) {
        return new AppState(
          new LoginState(baseUrl, credentials.username(), Optional.empty(), Optional.empty()),
          new DashboardState(state.dashboard().common(), Optional.empty(), Optional.empty(), Optional.empty(), false, state.dashboard().updates()),
          new UiState(true, false, "Connecting", true, state.ui().loginOverlayVisible())
        );
    }

    private AppState refresh(AppState state) {
        return new AppState(
          state.login(),
          new DashboardState(state.dashboard().common(), state.dashboard().radio(), state.dashboard().statusBar(), Optional.empty(), true, state.dashboard().updates()),
          new UiState(true, state.login().session().isPresent(), "Refreshing", true, state.ui().loginOverlayVisible())
        );
    }

    private AppState authenticate(AppState state, Session session, RadioState radio, StatusBarState statusBar) {
        return new AppState(
          new LoginState(state.login().baseUrl(), state.login().username(), Optional.of(session), Optional.empty()),
          new DashboardState(state.dashboard().common(), Optional.of(radio), Optional.of(statusBar), Optional.empty(), false, state.dashboard().updates() + 1),
          new UiState(false, true, "Connected", true, false)
        );
    }

    private AppState load(AppState state, RadioState radio, StatusBarState statusBar) {
        return new AppState(
          state.login(),
          new DashboardState(state.dashboard().common(), Optional.of(radio), Optional.of(statusBar), Optional.empty(), false, state.dashboard().updates() + 1),
          new UiState(false, state.login().session().isPresent(), "Updated", true, state.ui().loginOverlayVisible())
        );
    }

    private AppState common(AppState state, CommonDashboard common) {
        return new AppState(
          state.login(),
          new DashboardState(Optional.of(common), state.dashboard().radio(), state.dashboard().statusBar(), Optional.empty(), false, state.dashboard().updates() + 1),
          new UiState(false, state.login().session().isPresent(), "Common dashboard updated", true, state.ui().loginOverlayVisible())
        );
    }

    private AppState fail(AppState state, RouterFault fault) {
        Optional<Session> session = session(state.login().session(), fault);
        return new AppState(
          new LoginState(
            state.login().baseUrl(),
            state.login().username(),
            session,
            Optional.of(fault)
          ),
          new DashboardState(state.dashboard().common(), state.dashboard().radio(), state.dashboard().statusBar(), Optional.of(fault), false, state.dashboard().updates()),
          new UiState(false, session.isPresent(), note(fault), true, state.ui().loginOverlayVisible())
        );
    }

    private AppState withUi(AppState state, UiState ui) {
        return new AppState(state.login(), state.dashboard(), ui);
    }

    private Optional<Session> session(Optional<Session> session, RouterFault fault) {
        return switch (fault) {
            case RouterFault.AuthFault _, RouterFault.SessionExpiredFault _ ->
                Optional.empty();
            case RouterFault.TimeoutFault _, RouterFault.TransportFault _, RouterFault.ProtocolFault _, RouterFault.MalformedResponseFault _, RouterFault.UnsupportedCommandFault _ ->
                session;
        };
    }

    private String note(RouterFault fault) {
        return switch (fault) {
            case RouterFault.AuthFault _ ->
                "Authentication failed";
            case RouterFault.SessionExpiredFault _ ->
                "Session expired. Please sign in again.";
            case RouterFault.TimeoutFault _ ->
                "Router request timed out";
            case RouterFault.TransportFault _ ->
                "Router is unreachable";
            case RouterFault.ProtocolFault _, RouterFault.MalformedResponseFault _, RouterFault.UnsupportedCommandFault _ ->
                "Router response could not be processed";
        };
    }
}
