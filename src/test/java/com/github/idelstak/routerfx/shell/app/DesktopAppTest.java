package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.*;
import org.testfx.util.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
final class DesktopAppTest extends ApplicationTest {

    private FxStore fxStore;
    private Supplier<Result<Challenge>> challengeResult;
    private BiFunction<Credentials, Challenge, Result<Session>> loginResult;
    private Function<Session, Result<RadioState>> radioResult;
    private Supplier<Result<CommonDashboard>> commonResult;

    @BeforeEach
    void setUpApi() {
        challengeResult = () -> new Result.Success<>(new Challenge("tok"));
        loginResult = (credentials, challenge) -> new Result.Success<>(new Session("sess-1"));
        radioResult = session -> new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"));
        commonResult = () -> new Result.Success<>(common("4G+", "18:13:29"));
    }

    @Override
    public void start(Stage stage) {
        StateUpdate update = new StateUpdate();
        FlowEffects effect = new FlowEffects(baseUrl -> api());
        Store store = new Store(AppState.initial(), update, effect, command -> Thread.ofVirtual().start(command));
        fxStore = new FxStore(store);
        DashboardPane pane = new DashboardPane(fxStore);
        stage.setScene(new Scene(pane.root(), 900, 900));
        stage.show();
        fxStore.dispatch(new Msg.RefreshRequested());
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void preLoginRefreshShowsCommonNetworkType() {
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected pre-login common dashboard to show network type", lookup("#networkTypeValue").queryLabeled().getText(), is("4G+"));
    }

    @Test
    void preLoginUsesDefaultBaseUrl() {
        assertThat("Expected default base URL in login field", lookup("#baseUrlField").queryAs(TextField.class).getText(), is("http://192.168.1.1"));
    }

    @Test
    void preLoginUsesDefaultUsername() {
        assertThat("Expected default username in login field", lookup("#usernameField").queryAs(TextField.class).getText(), is("admin"));
    }

    @Test
    void preLoginHidesAuthenticatedPanel() {
        assertThat("Expected auth panel to remain hidden before login", lookup("#authPanel").query().isVisible(), is(false));
    }

    @Test
    void connectActionClearsPasswordField() {
        connect();
        assertThat("Expected password field to be cleared after connect", lookup("#passwordField").queryAs(PasswordField.class).getText(), is(""));
    }

    @Test
    void connectActionShowsConnectedNote() {
        connect();
        assertThat("Expected note to indicate connected state", lookup("#noteLabel").queryLabeled().getText(), is("Connected"));
    }

    @Test
    void connectActionShowsAuthenticatedPanel() {
        connect();
        assertThat("Expected connect action to reveal authenticated panel", lookup("#authPanel").query().isVisible(), is(true));
    }

    @Test
    void delayedConnectDisablesConnectButtonWhileLoading() {
        challengeResult = () -> {
            pauseMillis(350);
            return new Result.Success<>(new Challenge("tok"));
        };
        replaceText("#baseUrlField", "http://router.local");
        replaceText("#usernameField", "admin");
        replaceText("#passwordField", "pass");
        clickOn("#connectButton");
        WaitForAsyncUtils.sleep(60, TimeUnit.MILLISECONDS);
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected connect button to be disabled while login is in-flight", lookup("#connectButton").queryAs(Button.class).isDisabled(), is(true));
    }

    @Test
    void delayedConnectReEnablesConnectButtonAfterSuccess() {
        challengeResult = () -> {
            pauseMillis(250);
            return new Result.Success<>(new Challenge("tok"));
        };
        replaceText("#baseUrlField", "http://router.local");
        replaceText("#usernameField", "admin");
        replaceText("#passwordField", "pass");
        clickOn("#connectButton");
        waitForCondition(() -> !lookup("#connectButton").queryAs(Button.class).isDisabled());
        assertThat("Expected connect button to re-enable after login completes", lookup("#connectButton").queryAs(Button.class).isDisabled(), is(false));
    }

    @Test
    void delayedRefreshDisablesRefreshButtonWhileLoading() {
        commonResult = () -> {
            pauseMillis(350);
            return new Result.Success<>(common("4G+", "18:13:29"));
        };
        clickOn("#refreshButton");
        WaitForAsyncUtils.sleep(60, TimeUnit.MILLISECONDS);
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected refresh button to be disabled while refresh is in-flight", lookup("#refreshButton").queryAs(Button.class).isDisabled(), is(true));
    }

    @Test
    void connectActionShowsOperatorAfterLogin() {
        connect();
        assertThat("Expected authenticated panel to show operator value", lookup("#operatorValue").queryLabeled().getText(), is("Airtel Africa"));
    }

    @Test
    void refreshAfterLoginUpdatesUptimeValue() {
        AtomicInteger fetches = new AtomicInteger();
        radioResult = session -> {
            if (fetches.getAndIncrement() == 0) {
                return new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"));
            }
            return new Result.Success<>(radio("Airtel Africa", "0-8-2-20", "8300.00"));
        };
        connect();
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected uptime value to change on refresh after login", lookup("#uptimeValue").queryLabeled().getText(), is("0-8-2-20"));
    }

    @Test
    void refreshAfterLoginUpdatesTrafficValue() {
        AtomicInteger fetches = new AtomicInteger();
        radioResult = session -> {
            if (fetches.getAndIncrement() == 0) {
                return new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"));
            }
            return new Result.Success<>(radio("Airtel Africa", "0-8-2-20", "9000.00"));
        };
        connect();
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected current traffic value to change on refresh after login", lookup("#currentFlowValue").queryLabeled().getText(), is("9000.00/239.55"));
    }

    @Test
    void refreshBeforeLoginCanUpdateCommonDashboard() {
        commonResult = () -> new Result.Success<>(common("5G", "19:00:00"));
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected refresh before login to update common network type", lookup("#networkTypeValue").queryLabeled().getText(), is("5G"));
    }

    @Test
    void connectFailureShowsFailedNote() {
        loginResult = (credentials, challenge) -> new Result.Failure<>(new RouterFault.AuthFault("wrong password"));
        connect();
        assertThat("Expected failed login to set failed note", lookup("#noteLabel").queryLabeled().getText(), is("Failed"));
    }

    @Test
    void connectFailureKeepsAuthenticatedPanelHidden() {
        loginResult = (credentials, challenge) -> new Result.Failure<>(new RouterFault.AuthFault("wrong password"));
        connect();
        assertThat("Expected failed login to keep auth panel hidden", lookup("#authPanel").query().isVisible(), is(false));
    }

    @Test
    void refreshFailureAfterLoginPreservesCurrentOperator() {
        AtomicBoolean first = new AtomicBoolean(true);
        radioResult = session -> {
            if (first.getAndSet(false)) {
                return new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"));
            }
            return new Result.Failure<>(new RouterFault.TimeoutFault("timed out"));
        };
        connect();
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected existing operator to remain when refresh fails", lookup("#operatorValue").queryLabeled().getText(), is("Airtel Africa"));
    }

    private void replaceText(String fieldId, String text) {
        clickOn(fieldId);
        press(KeyCode.CONTROL, KeyCode.A);
        release(KeyCode.A, KeyCode.CONTROL);
        write(text);
    }

    private void connect() {
        replaceText("#baseUrlField", "http://router.local");
        replaceText("#usernameField", "admin");
        replaceText("#passwordField", "pass");
        clickOn("#connectButton");
        waitForCondition(() -> lookup("#authPanel").query().isVisible() || "Failed".equals(lookup("#noteLabel").queryLabeled().getText()));
    }

    private void waitForCondition(Supplier<Boolean> condition) {
        try {
            WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, condition::get);
        } catch (TimeoutException issue) {
            fail("Condition timed out: " + issue.getMessage());
        }
    }

    private void pauseMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interruptedIssue) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(interruptedIssue);
        }
    }

    private RadioState radio(String operator, String uptime, String flowDl) {
        return new RadioState(operator, "4G+", "-77", "-78", "-10", "10", "1+3", "15+10", flowDl, "239.55", "2026-04-11 03:00:46", uptime);
    }

    private CommonDashboard common(String networkType, String runningTime) {
        return new CommonDashboard(networkType, "SIM", "AT", "2.4 GHz Wi-Fi", "5 GHz Wi-Fi", "LAN2", "-77dBm/-", "-78dBm/-", "-10dB/-", "10dB/-", "475+475/-", "124+1351/-", "10.129.71.22", "4E:E4:E8:D6:57:2A", "102.216.71.102", "8.8.8.8", "-", "-", "-", runningTime, "4.15.6", "All Direction");
    }

    private RouterApi api() {
        return new RouterApi() {
            @Override
            public Result<Challenge> fetchChallenge() {
                return challengeResult.get();
            }

            @Override
            public Result<Session> login(Credentials credentials, Challenge challenge) {
                return loginResult.apply(credentials, challenge);
            }

            @Override
            public Result<RadioState> fetchRadioState(Session session) {
                return radioResult.apply(session);
            }

            @Override
            public Result<CommonDashboard> fetchCommonDashboard() {
                return commonResult.get();
            }
        };
    }
}
