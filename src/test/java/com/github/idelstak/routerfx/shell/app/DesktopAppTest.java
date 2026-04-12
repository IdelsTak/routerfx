package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;
import javafx.scene.*;
import javafx.stage.*;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.*;
import org.testfx.util.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Tag("ui")
final class DesktopAppTest extends ApplicationTest {

    private Store store;

    @Override
    public void start(Stage stage) {
        var update = new StateUpdate();
        var effect = new FlowEffects(baseUrl -> api());
        store = new Store(AppState.initial(), update, effect, Runnable::run);
        var pane = new DashboardPane(store);
        stage.setScene(new Scene(pane.root(), 900, 900));
        stage.show();
        store.dispatch(new Msg.RefreshRequested());
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void preLoginRefreshShowsCommonNetworkType() {
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected pre-login common dashboard to show network type", lookup("#networkTypeValue").queryLabeled().getText(), is("4G+"));
    }

    @Test
    void connectActionShowsAuthenticatedPanel() {
        clickOn("#baseUrlField").eraseText(64).write("http://router.local");
        clickOn("#usernameField").eraseText(32).write("admin");
        clickOn("#passwordField").write("päss");
        clickOn("#connectButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected connect action to reveal authenticated panel", lookup("#authPanel").query().isVisible(), is(true));
    }

    @Test
    void connectActionShowsOperatorAfterLogin() {
        clickOn("#baseUrlField").eraseText(64).write("http://router.local");
        clickOn("#usernameField").eraseText(32).write("admin");
        clickOn("#passwordField").write("päss");
        clickOn("#connectButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected authenticated panel to show operator value", lookup("#operatorValue").queryLabeled().getText(), is("Airtel Africa"));
    }

    private RouterApi api() {
        return new RouterApi() {
            @Override
            public Result<Challenge> fetchChallenge() {
                return new Result.Success<>(new Challenge("tok"));
            }

            @Override
            public Result<Session> login(Credentials credentials, Challenge challenge) {
                return new Result.Success<>(new Session("sess-1"));
            }

            @Override
            public Result<RadioState> fetchRadioState(Session session) {
                return new Result.Success<>(new RadioState("Airtel Africa", "4G+", "-77", "-78", "-10", "10", "1+3", "15+10", "8298.12", "239.55", "2026-04-11 03:00:46", "0-8-2-19"));
            }

            @Override
            public Result<CommonDashboard> fetchCommonDashboard() {
                return new Result.Success<>(new CommonDashboard("4G+", "SIM", "AT", "2.4 GHz Wi-Fi", "5 GHz Wi-Fi", "LAN2", "-77dBm/-", "-78dBm/-", "-10dB/-", "10dB/-", "475+475/-", "124+1351/-", "10.129.71.22", "4E:E4:E8:D6:57:2A", "102.216.71.102", "8.8.8.8", "-", "-", "-", "18:13:29", "4.15.6", "All Direction"));
            }
        };
    }
}
