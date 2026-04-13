package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class StateUpdateTest {

    @Test
    void loginOverlayOpenedMarksOverlayAsVisible() {
        var update = new StateUpdate();
        var state = update.apply(AppState.initial(), new Msg.LoginOverlayOpened());
        assertThat("Expected login-overlay-opened transition to show login overlay", state.ui().loginOverlayVisible(), is(true));
    }

    @Test
    void loginOverlayClosedMarksOverlayAsHidden() {
        var update = new StateUpdate();
        var opened = update.apply(AppState.initial(), new Msg.LoginOverlayOpened());
        var state = update.apply(opened, new Msg.LoginOverlayClosed());
        assertThat("Expected login-overlay-closed transition to hide login overlay", state.ui().loginOverlayVisible(), is(false));
    }

    @Test
    void connectRequestedMarksStateAsBusyAndDisconnected() {
        var update = new StateUpdate();
        var state = update.apply(
          AppState.initial(),
          new Msg.ConnectRequested("http://192.168.1.1", new Credentials("admin", "päss-42"))
        );
        assertThat("Expected connect-requested transition to mark app busy", state.ui().busy(), is(true));
    }

    @Test
    void authenticatedStoresSessionAndEnablesRefresh() {
        var update = new StateUpdate();
        var state = update.apply(
          AppState.initial(),
          new Msg.Authenticated(new Session("sess-3"), radio(), statusBar())
        );
        assertThat("Expected authenticated transition to enable refresh", state.ui().canRefresh(), is(true));
    }

    @Test
    void failedWithSessionExpiryClearsSession() {
        var update = new StateUpdate();
        var afterAuth = update.apply(
          AppState.initial(),
          new Msg.Authenticated(new Session("sess-7"), radio(), statusBar())
        );
        var state = update.apply(afterAuth, new Msg.Failed(new RouterFault.SessionExpiredFault("expired")));
        assertThat("Expected session-expiry fault to clear session", state.login().session().isEmpty(), is(true));
    }

    @Test
    void failedWithSessionExpiryMarksStateAsDisconnected() {
        var update = new StateUpdate();
        var afterAuth = update.apply(
          AppState.initial(),
          new Msg.Authenticated(new Session("sess-7"), radio(), statusBar())
        );
        var state = update.apply(afterAuth, new Msg.Failed(new RouterFault.SessionExpiredFault("expired")));
        assertThat("Expected session-expiry fault to set UI as disconnected", state.ui().connected(), is(false));
    }

    @Test
    void failedWithSessionExpiryShowsReconnectMessage() {
        var update = new StateUpdate();
        var afterAuth = update.apply(
          AppState.initial(),
          new Msg.Authenticated(new Session("sess-7"), radio(), statusBar())
        );
        var state = update.apply(afterAuth, new Msg.Failed(new RouterFault.SessionExpiredFault("expired")));
        assertThat("Expected session-expiry fault to show reconnect guidance", state.ui().note(), is("Session expired. Please sign in again."));
    }

    @Test
    void failedWithAuthFaultShowsAuthenticationMessage() {
        var update = new StateUpdate();
        var state = update.apply(AppState.initial(), new Msg.Failed(new RouterFault.AuthFault("invalid")));
        assertThat("Expected auth fault to show authentication message", state.ui().note(), is("Authentication failed"));
    }

    private RadioState radio() {
        return new RadioState("Åirtel", "LTE", "-91", "-63", "-12", "18", "B3", "20M", "10", "3", "00:01:00", "60");
    }

    private StatusBarState statusBar() {
        return new StatusBarState("4", "LTE", "SIM", "0");
    }
}
