package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
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
    private Function<Session, Result<StatusBarState>> statusBarResult;
    private Supplier<Result<CommonDashboard>> commonResult;

    @BeforeEach
    void setUpApi() {
        challengeResult = () -> new Result.Success<>(new Challenge("tok"));
        loginResult = (credentials, challenge) -> new Result.Success<>(new Session("sess-1"));
        radioResult = session -> new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"));
        statusBarResult = session -> new Result.Success<>(statusBar("5", "4G+", "SIM", "0"));
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
    void preLoginKeepsLoginOverlayHidden() {
        assertThat("Expected login overlay to be hidden before explicit login action", lookup("#loginOverlay").query().isVisible(), is(false));
    }

    @Test
    void preLoginRendersFourDistinctCardSections() {
        assertThat("Expected top status card to be visible", lookup("#topStatusCard").query().isVisible(), is(true));
        assertThat("Expected signal card to be visible", lookup("#signalCard").query().isVisible(), is(true));
        assertThat("Expected network path card to be visible", lookup("#networkPathCard").query().isVisible(), is(true));
        assertThat("Expected footer stats card to be visible", lookup("#footerStatsCard").query().isVisible(), is(true));
    }

    @Test
    void preLoginShowsNetworkPathNodes() {
        assertThat("Expected router node to be visible in network path card", lookup("#routerPathNode").query().isVisible(), is(true));
        assertThat("Expected internet node to be visible in network path card", lookup("#internetPathNode").query().isVisible(), is(true));
        assertThat("Expected primary DNS node to be visible in network path card", lookup("#primaryDnsPathNode").query().isVisible(), is(true));
        assertThat("Expected secondary DNS node to be visible in network path card", lookup("#secondaryDnsPathNode").query().isVisible(), is(true));
    }

    @Test
    void rsrpGaugeAppliesExpectedZoneColorForEveryLevel() {
        AtomicInteger level = new AtomicInteger(-140);
        commonResult = () -> new Result.Success<>(commonWithRsrp("4G+", "18:13:29", level.get()));
        List<String> failures = new ArrayList<>();
        Arc track = lookup("#rsrpGaugeTrack").queryAs(Arc.class);
        Arc fill = lookup("#rsrpGaugeFill").queryAs(Arc.class);
        for (int rsrp = -140; rsrp <= -44; rsrp++) {
            final int currentRsrp = rsrp;
            level.set(rsrp);
            clickOn("#refreshButton");
            waitForCondition(() -> lookup("#rsrpValue").queryLabeled().getText().contains(Integer.toString(currentRsrp)));
            double expectedLength = expectedRsrpFillLength(currentRsrp);
            if (Math.abs(fill.getLength() - expectedLength) > 0.001d) {
                failures.add(currentRsrp + " expected fill length " + expectedLength + " but was " + fill.getLength());
            }
            String expectedTone = expectedRsrpToneClass(currentRsrp);
            if (!hasTone(fill, expectedTone)) {
                failures.add(currentRsrp + " expected fill tone " + expectedTone);
            }
            if (Math.abs(track.getLength() + 180d) > 0.001d) {
                failures.add(currentRsrp + " expected static track length -180.0 but was " + track.getLength());
            }
        }
        assertTrue(failures.isEmpty(), "Expected gauge segments for all RSRP levels to map correctly; mismatches: " + String.join(", ", failures));
    }

    @Test
    void rsrpChipFollowsQualityToneTextAndColorsForEveryLevel() {
        AtomicInteger level = new AtomicInteger(-140);
        commonResult = () -> new Result.Success<>(commonWithRsrp("4G+", "18:13:29", level.get()));
        List<String> failures = new ArrayList<>();
        Parent chip = lookup("#signalChip").queryAs(Parent.class);
        Label chipDot = lookup("#signalChipDot").queryAs(Label.class);
        Label chipText = lookup("#signalChipText").queryAs(Label.class);
        for (int rsrp = -140; rsrp <= -44; rsrp++) {
            final int currentRsrp = rsrp;
            level.set(rsrp);
            clickOn("#refreshButton");
            waitForCondition(() -> lookup("#rsrpValue").queryLabeled().getText().contains(Integer.toString(currentRsrp)));

            String expectedTone = expectedRsrpChipToneClass(currentRsrp);
            if (!chip.getStyleClass().contains(expectedTone)) {
                failures.add(currentRsrp + " expected chip tone " + expectedTone + " but had " + chip.getStyleClass());
            }

            String expectedQuality = expectedRsrpQuality(currentRsrp);
            if (!expectedQuality.equals(chipText.getText())) {
                failures.add(currentRsrp + " expected chip text " + expectedQuality + " but was " + chipText.getText());
            }

            Color expectedDot = expectedChipDotColor(currentRsrp);
            if (!paintMatches(chipDot.getTextFill(), expectedDot)) {
                failures.add(currentRsrp + " expected chip dot color " + expectedDot + " but was " + chipDot.getTextFill());
            }

            Color expectedTextColor = expectedChipTextColor(currentRsrp);
            if (!paintMatches(chipText.getTextFill(), expectedTextColor)) {
                failures.add(currentRsrp + " expected chip text color " + expectedTextColor + " but was " + chipText.getTextFill());
            }
        }
        assertTrue(failures.isEmpty(), "Expected chip tone/text/colors for all RSRP levels to map correctly; mismatches: " + String.join(", ", failures));
    }

    @Test
    void rsrpCaptionFollowsDescriptionRangeForEveryLevel() {
        AtomicInteger level = new AtomicInteger(-140);
        commonResult = () -> new Result.Success<>(commonWithRsrp("4G+", "18:13:29", level.get()));
        List<String> failures = new ArrayList<>();
        Label caption = lookup("#signalHeroCaption").queryAs(Label.class);
        for (int rsrp = -140; rsrp <= -44; rsrp++) {
            final int currentRsrp = rsrp;
            level.set(rsrp);
            clickOn("#refreshButton");
            waitForCondition(() -> lookup("#rsrpValue").queryLabeled().getText().contains(Integer.toString(currentRsrp)));
            String expectedCaption = expectedRsrpDescription(currentRsrp);
            if (!expectedCaption.equals(caption.getText())) {
                failures.add(currentRsrp + " expected caption " + expectedCaption + " but was " + caption.getText());
            }
        }
        assertTrue(failures.isEmpty(), "Expected caption descriptions for all RSRP levels to map correctly; mismatches: " + String.join(", ", failures));
    }

    @Test
    void supportingSignalQualityChipsFollowDefinedThresholds() {
        record MetricCase(int rssi, int sinr, int rsrq,
                          String rssiLabel, String rssiTone, String rssiRangeTone,
                          String sinrLabel, String sinrTone, String sinrRangeTone,
                          String rsrqLabel, String rsrqTone, String rsrqRangeTone) {
        }
        List<MetricCase> cases = List.of(
          new MetricCase(-120, -6, -22, "No signal", "metric-chip-tone-nosignal", "metric-range-fill-nosignal", "Interference dominant", "metric-chip-tone-nosignal", "metric-range-fill-nosignal", "Severe congestion", "metric-chip-tone-nosignal", "metric-range-fill-nosignal"),
          new MetricCase(-105, -1, -16, "Poor", "metric-chip-tone-poor", "metric-range-fill-poor", "High interference", "metric-chip-tone-poor", "metric-range-fill-poor", "High congestion", "metric-chip-tone-poor", "metric-range-fill-poor"),
          new MetricCase(-95, 5, -12, "Fair", "metric-chip-tone-fair", "metric-range-fill-fair", "Moderate interference", "metric-chip-tone-fair", "metric-range-fill-fair", "Moderate congestion", "metric-chip-tone-fair", "metric-range-fill-fair"),
          new MetricCase(-85, 15, -6, "Good", "metric-chip-tone-good", "metric-range-fill-good", "Low interference", "metric-chip-tone-good", "metric-range-fill-good", "Low congestion", "metric-chip-tone-good", "metric-range-fill-good"),
          new MetricCase(-70, 25, -2, "Excellent", "metric-chip-tone-excellent", "metric-range-fill-excellent", "Clean signal", "metric-chip-tone-excellent", "metric-range-fill-excellent", "Clear channel", "metric-chip-tone-excellent", "metric-range-fill-excellent")
        );
        List<String> failures = new ArrayList<>();
        Label rssiChip = lookup("#rssiQualityChip").queryAs(Label.class);
        Label sinrChip = lookup("#sinrQualityChip").queryAs(Label.class);
        Label rsrqChip = lookup("#rsrqQualityChip").queryAs(Label.class);
        AnchorPane rssiRangeTrack = lookup("#rssiRangeTrack").queryAs(AnchorPane.class);
        Region rssiRangeFill = lookup("#rssiRangeFill").queryAs(Region.class);
        AnchorPane sinrRangeTrack = lookup("#sinrRangeTrack").queryAs(AnchorPane.class);
        Region sinrRangeFill = lookup("#sinrRangeFill").queryAs(Region.class);
        AnchorPane rsrqRangeTrack = lookup("#rsrqRangeTrack").queryAs(AnchorPane.class);
        Region rsrqRangeFill = lookup("#rsrqRangeFill").queryAs(Region.class);
        for (MetricCase metricCase : cases) {
            commonResult = () -> new Result.Success<>(commonWithMetrics("4G+", "18:13:29", -77, metricCase.rssi(), metricCase.rsrq(), metricCase.sinr()));
            clickOn("#refreshButton");
            WaitForAsyncUtils.waitForFxEvents();
            checkMetricChip("RSSI", metricCase.rssiLabel(), metricCase.rssiTone(), rssiChip, failures);
            checkMetricChip("SINR", metricCase.sinrLabel(), metricCase.sinrTone(), sinrChip, failures);
            checkMetricChip("RSRQ", metricCase.rsrqLabel(), metricCase.rsrqTone(), rsrqChip, failures);
            checkMetricRangeFill("RSSI", metricCase.rssiRangeTone(), rssiRangeFill, failures);
            checkMetricRangeFill("SINR", metricCase.sinrRangeTone(), sinrRangeFill, failures);
            checkMetricRangeFill("RSRQ", metricCase.rsrqRangeTone(), rsrqRangeFill, failures);
            checkMetricRangeGeometry("RSSI", normalizedRatio(metricCase.rssi(), -110d, -50d), rssiRangeTrack, rssiRangeFill, failures);
            checkMetricRangeGeometry("SINR", normalizedRatio(metricCase.sinr(), -3d, 20d), sinrRangeTrack, sinrRangeFill, failures);
            checkMetricRangeGeometry("RSRQ", normalizedRatio(metricCase.rsrq(), -20d, -5d), rsrqRangeTrack, rsrqRangeFill, failures);
        }
        assertTrue(failures.isEmpty(), "Expected supporting metric chip mappings to match thresholds; mismatches: " + String.join(", ", failures));
    }

    @Test
    void connectButtonShowsLoginOverlay() {
        clickOn("#connectButton");
        assertThat("Expected connect button to show login overlay", lookup("#loginOverlay").query().isVisible(), is(true));
    }

    @Test
    void shellScrollPaneKeepsHorizontalBarHidden() {
        assertThat("Expected shell scroll pane to keep horizontal bar policy set to NEVER", lookup(".shell-scroll").queryAs(ScrollPane.class).getHbarPolicy(), is(ScrollPane.ScrollBarPolicy.NEVER));
    }

    @Test
    void shellScrollPaneFitsContentWidth() {
        assertThat("Expected shell scroll pane to fit content width", lookup(".shell-scroll").queryAs(ScrollPane.class).isFitToWidth(), is(true));
    }

    @Test
    void shellRootLoadsStylesheetsFromFxml() {
        assertThat("Expected shell root to load stylesheets directly from FXML", lookup("#shellRoot").queryAs(Parent.class).getStylesheets().size(), is(3));
    }

    @Test
    void connectButtonShowsVisibleKeyboardFocusBorder() {
        Button button = lookup("#connectButton").queryAs(Button.class);
        interact(button::requestFocus);
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected connect button focus border to use visible warning accent", borderColor(button), is(Color.web("#ba7517")));
    }

    @Test
    void baseUrlFieldShowsVisibleKeyboardFocusBorder() {
        clickOn("#connectButton");
        waitForCondition(() -> lookup("#loginOverlay").query().isVisible());
        TextField field = lookup("#baseUrlField").queryAs(TextField.class);
        interact(field::requestFocus);
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected base URL field focus border to use visible warning accent", borderColor(field), is(Color.web("#ba7517")));
    }

    @Test
    void signalChipTextMaintainsReadableContrastAcrossAllQualities() {
        List<String> failures = new ArrayList<>();
        checkContrast("No signal", Color.web("#8d2f2f"), compositeOnWhite(Color.web("#e24b4a"), 0.18d), 4.5d, failures);
        checkContrast("Poor", Color.web("#9a5050"), Color.web("#fde9e9"), 4.5d, failures);
        checkContrast("Fair", Color.web("#633806"), Color.web("#faeeda"), 4.5d, failures);
        checkContrast("Good", Color.web("#4d6a2d"), Color.web("#eff7e2"), 4.5d, failures);
        checkContrast("Excellent", Color.web("#27500a"), Color.web("#eaf3de"), 4.5d, failures);
        assertTrue(failures.isEmpty(), "Expected signal chip text contrast to remain readable for every quality level; mismatches: " + String.join(", ", failures));
    }

    @Test
    void rsrpQualityUsesTextLabelsAndDescriptionsNotColorOnly() {
        List<String> failures = new ArrayList<>();
        checkRsrpNonColorCue(-120, "No signal", "Connection unusable or dropped", failures);
        checkRsrpNonColorCue(-105, "Poor", "Drops likely, very slow speeds", failures);
        checkRsrpNonColorCue(-95, "Fair", "Usable but inconsistent", failures);
        checkRsrpNonColorCue(-85, "Good", "Reliable for most tasks", failures);
        checkRsrpNonColorCue(-70, "Excellent", "Strong, stable, fast speeds", failures);
        assertTrue(failures.isEmpty(), "Expected RSRP to communicate quality with text labels and captions; mismatches: " + String.join(", ", failures));
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
    void successfulLoginSubmitHidesLoginOverlay() {
        connect();
        assertThat("Expected login overlay to be hidden after successful authentication", lookup("#loginOverlay").query().isVisible(), is(false));
    }

    @Test
    void delayedConnectDisablesConnectButtonWhileLoading() {
        challengeResult = () -> {
            pauseMillis(350);
            return new Result.Success<>(new Challenge("tok"));
        };
        clickOn("#connectButton");
        replaceText("#baseUrlField", "http://router.local");
        replaceText("#usernameField", "admin");
        replaceText("#passwordField", "pass");
        submitLogin();
        WaitForAsyncUtils.sleep(60, TimeUnit.MILLISECONDS);
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected login submit button to be disabled while login is in-flight", lookup("#loginSubmitButton").queryAs(Button.class).isDisabled(), is(true));
    }

    @Test
    void delayedConnectReEnablesConnectButtonAfterSuccess() {
        challengeResult = () -> {
            pauseMillis(250);
            return new Result.Success<>(new Challenge("tok"));
        };
        clickOn("#connectButton");
        replaceText("#baseUrlField", "http://router.local");
        replaceText("#usernameField", "admin");
        replaceText("#passwordField", "pass");
        submitLogin();
        waitForCondition(() -> !lookup("#loginSubmitButton").queryAs(Button.class).isDisabled());
        assertThat("Expected login submit button to re-enable after login completes", lookup("#loginSubmitButton").queryAs(Button.class).isDisabled(), is(false));
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
    void connectActionShowsStatusSignalAfterLogin() {
        connect();
        assertThat("Expected authenticated status panel to show signal level", lookup("#statusSignalValue").queryLabeled().getText(), is("5"));
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
        assertThat("Expected failed login to set authentication note", lookup("#noteLabel").queryLabeled().getText(), is("Authentication failed"));
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

    @Test
    void periodicRefreshBeforeLoginUpdatesCommonDashboardWithoutManualRefresh() {
        commonResult = () -> new Result.Success<>(common("5G", "19:00:00"));
        waitForCondition(() -> "5G".equals(lookup("#networkTypeValue").queryLabeled().getText()), 8);
        assertThat("Expected periodic refresh to update common dashboard before login", lookup("#networkTypeValue").queryLabeled().getText(), is("5G"));
    }

    @Test
    void sessionExpiryAfterLoginShowsReconnectMessage() {
        AtomicBoolean first = new AtomicBoolean(true);
        radioResult = session -> first.getAndSet(false)
          ? new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"))
          : new Result.Failure<>(new RouterFault.SessionExpiredFault("expired"));
        connect();
        clickOn("#refreshButton");
        waitForCondition(() -> "Session expired. Please sign in again.".equals(lookup("#noteLabel").queryLabeled().getText()));
        assertThat("Expected session-expiry refresh to show reconnect message", lookup("#noteLabel").queryLabeled().getText(), is("Session expired. Please sign in again."));
    }

    @Test
    void sessionExpiryAfterLoginHidesAuthenticatedPanel() {
        AtomicBoolean first = new AtomicBoolean(true);
        radioResult = session -> first.getAndSet(false)
          ? new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"))
          : new Result.Failure<>(new RouterFault.SessionExpiredFault("expired"));
        connect();
        clickOn("#refreshButton");
        waitForCondition(() -> !lookup("#authPanel").query().isVisible());
        assertThat("Expected session-expiry refresh to hide authenticated panel", lookup("#authPanel").query().isVisible(), is(false));
    }

    @Test
    void sessionExpiryAfterLoginStillAllowsCommonRefresh() {
        AtomicBoolean first = new AtomicBoolean(true);
        radioResult = session -> first.getAndSet(false)
          ? new Result.Success<>(radio("Airtel Africa", "0-8-2-19", "8298.12"))
          : new Result.Failure<>(new RouterFault.SessionExpiredFault("expired"));
        connect();
        clickOn("#refreshButton");
        waitForCondition(() -> "Session expired. Please sign in again.".equals(lookup("#noteLabel").queryLabeled().getText()));
        commonResult = () -> new Result.Success<>(common("5G", "19:00:00"));
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertThat("Expected common dashboard refresh to work after session-expiry fallback", lookup("#networkTypeValue").queryLabeled().getText(), is("5G"));
    }

    private void replaceText(String fieldId, String text) {
        TextInputControl field = lookup(fieldId).queryAs(TextInputControl.class);
        interact(field::clear);
        interact(() -> field.setText(text));
    }

    private void connect() {
        clickOn("#connectButton");
        waitForCondition(() -> lookup("#loginOverlay").query().isVisible());
        replaceText("#baseUrlField", "http://router.local");
        replaceText("#usernameField", "admin");
        replaceText("#passwordField", "pass");
        submitLogin();
        waitForCondition(() -> {
            String note = lookup("#noteLabel").queryLabeled().getText();
            return lookup("#authPanel").query().isVisible()
              || "Authentication failed".equals(note)
              || "Connected".equals(note);
        });
    }

    private void submitLogin() {
        waitForCondition(() -> {
            Button submit = lookup("#loginSubmitButton").queryAs(Button.class);
            return submit.isVisible() && !submit.isDisabled();
        });
        Button submit = lookup("#loginSubmitButton").queryAs(Button.class);
        interact(submit::fire);
    }

    private void waitForCondition(Supplier<Boolean> condition) {
        waitForCondition(condition, 5);
    }

    private void waitForCondition(Supplier<Boolean> condition, int seconds) {
        try {
            WaitForAsyncUtils.waitFor(seconds, TimeUnit.SECONDS, condition::get);
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

    private CommonDashboard commonWithRsrp(String networkType, String runningTime, int rsrp) {
        return new CommonDashboard(networkType, "SIM", "AT", "2.4 GHz Wi-Fi", "5 GHz Wi-Fi", "LAN2", rsrp + "dBm/-", "-78dBm/-", "-10dB/-", "10dB/-", "475+475/-", "124+1351/-", "10.129.71.22", "4E:E4:E8:D6:57:2A", "102.216.71.102", "8.8.8.8", "-", "-", "-", runningTime, "4.15.6", "All Direction");
    }

    private CommonDashboard commonWithMetrics(String networkType, String runningTime, int rsrp, int rssi, int rsrq, int sinr) {
        return new CommonDashboard(networkType, "SIM", "AT", "2.4 GHz Wi-Fi", "5 GHz Wi-Fi", "LAN2", rsrp + "dBm/-", rssi + "dBm/-", rsrq + "dB/-", sinr + "dB/-", "475+475/-", "124+1351/-", "10.129.71.22", "4E:E4:E8:D6:57:2A", "102.216.71.102", "8.8.8.8", "-", "-", "-", runningTime, "4.15.6", "All Direction");
    }

    private double expectedRsrpFillLength(int rsrp) {
        double clamped = Math.max(-140d, Math.min(-44d, rsrp));
        double ratio = (clamped + 140d) / 96d;
        return -1d * ratio * 180d;
    }

    private String expectedRsrpToneClass(int rsrp) {
        if (rsrp <= -110) {
            return "signal-gauge-tone-nosignal";
        }
        if (rsrp <= -100) {
            return "signal-gauge-tone-poor";
        }
        if (rsrp <= -90) {
            return "signal-gauge-tone-fair";
        }
        if (rsrp <= -80) {
            return "signal-gauge-tone-good";
        }
        return "signal-gauge-tone-excellent";
    }

    private String expectedRsrpChipToneClass(int rsrp) {
        if (rsrp <= -110) {
            return "signal-chip-tone-nosignal";
        }
        if (rsrp <= -100) {
            return "signal-chip-tone-poor";
        }
        if (rsrp <= -90) {
            return "signal-chip-tone-fair";
        }
        if (rsrp <= -80) {
            return "signal-chip-tone-good";
        }
        return "signal-chip-tone-excellent";
    }

    private String expectedRsrpQuality(int rsrp) {
        if (rsrp <= -110) {
            return "No signal";
        }
        if (rsrp <= -100) {
            return "Poor";
        }
        if (rsrp <= -90) {
            return "Fair";
        }
        if (rsrp <= -80) {
            return "Good";
        }
        return "Excellent";
    }

    private String expectedRsrpDescription(int rsrp) {
        if (rsrp <= -110) {
            return "Connection unusable or dropped";
        }
        if (rsrp <= -100) {
            return "Drops likely, very slow speeds";
        }
        if (rsrp <= -90) {
            return "Usable but inconsistent";
        }
        if (rsrp <= -80) {
            return "Reliable for most tasks";
        }
        return "Strong, stable, fast speeds";
    }

    private Color expectedChipDotColor(int rsrp) {
        if (rsrp <= -110) {
            return Color.web("#e24b4a");
        }
        if (rsrp <= -100) {
            return Color.web("#f09595");
        }
        if (rsrp <= -90) {
            return Color.web("#ef9f27");
        }
        if (rsrp <= -80) {
            return Color.web("#c0dd97");
        }
        return Color.web("#639922");
    }

    private Color expectedChipTextColor(int rsrp) {
        if (rsrp <= -110) {
            return Color.web("#8d2f2f");
        }
        if (rsrp <= -100) {
            return Color.web("#9a5050");
        }
        if (rsrp <= -90) {
            return Color.web("#633806");
        }
        if (rsrp <= -80) {
            return Color.web("#4d6a2d");
        }
        return Color.web("#27500A");
    }

    private boolean paintMatches(Paint actual, Color expected) {
        if (!(actual instanceof Color color)) {
            return false;
        }
        double epsilon = 0.01d;
        return Math.abs(color.getRed() - expected.getRed()) < epsilon
          && Math.abs(color.getGreen() - expected.getGreen()) < epsilon
          && Math.abs(color.getBlue() - expected.getBlue()) < epsilon
          && Math.abs(color.getOpacity() - expected.getOpacity()) < epsilon;
    }

    private boolean hasTone(Arc arc, String tone) {
        return arc.getStyleClass().contains(tone);
    }

    private void checkMetricChip(String metric, String expectedText, String expectedTone, Label chip, List<String> failures) {
        if (!expectedText.equals(chip.getText())) {
            failures.add(metric + " expected chip text " + expectedText + " but was " + chip.getText());
        }
        if (!chip.getStyleClass().contains(expectedTone)) {
            failures.add(metric + " expected chip tone " + expectedTone + " but had " + chip.getStyleClass());
        }
    }

    private void checkMetricRangeFill(String metric, String expectedTone, Region fill, List<String> failures) {
        if (!fill.getStyleClass().contains(expectedTone)) {
            failures.add(metric + " expected range fill tone " + expectedTone + " but had " + fill.getStyleClass());
        }
    }

    private void checkRsrpNonColorCue(int rsrp, String expectedLabel, String expectedCaption, List<String> failures) {
        commonResult = () -> new Result.Success<>(commonWithRsrp("4G+", "18:13:29", rsrp));
        clickOn("#refreshButton");
        WaitForAsyncUtils.waitForFxEvents();
        Label chip = lookup("#signalChipText").queryAs(Label.class);
        Label caption = lookup("#signalHeroCaption").queryAs(Label.class);
        if (!expectedLabel.equals(chip.getText())) {
            failures.add(rsrp + " expected non-color quality label " + expectedLabel + " but was " + chip.getText());
        }
        if (!expectedCaption.equals(caption.getText())) {
            failures.add(rsrp + " expected non-color quality caption " + expectedCaption + " but was " + caption.getText());
        }
    }

    private void checkMetricRangeGeometry(String metric, double expectedRatio, AnchorPane track, Region fill, List<String> failures) {
        var trackBounds = track.getBoundsInLocal();
        var fillBounds = fill.getBoundsInParent();
        var trackWidth = trackBounds.getWidth();
        var trackHeight = trackBounds.getHeight();
        var fillWidth = fillBounds.getWidth();
        var fillHeight = fillBounds.getHeight();

        if (trackWidth <= 0d || trackHeight <= 0d) {
            failures.add(metric + " expected positive track dimensions but got width=" + trackWidth + ", height=" + trackHeight);
            return;
        }

        var expectedWidth = trackWidth * expectedRatio;
        if (Math.abs(fillWidth - expectedWidth) > 2d) {
            failures.add(metric + " expected fill width " + expectedWidth + " but was " + fillWidth);
        }
        if (Math.abs(fillHeight - trackHeight) > 1d) {
            failures.add(metric + " expected fill height " + trackHeight + " but was " + fillHeight);
        }
        if (expectedRatio < 0.99d && fillWidth >= trackWidth - 1d) {
            failures.add(metric + " expected partial fill below full track width but got fill=" + fillWidth + " track=" + trackWidth);
        }
    }

    private double normalizedRatio(double value, double min, double max) {
        var clamped = Math.max(min, Math.min(max, value));
        return (clamped - min) / (max - min);
    }

    private Color borderColor(Control control) {
        var border = control.getBorder();
        if (border == null || border.getStrokes().isEmpty()) {
            return Color.TRANSPARENT;
        }
        var stroke = border.getStrokes().getFirst().getTopStroke();
        if (stroke instanceof Color color) {
            return color;
        }
        return Color.TRANSPARENT;
    }

    private void checkContrast(String quality, Color foreground, Color background, double minimum, List<String> failures) {
        var ratio = contrastRatio(foreground, background);
        if (ratio < minimum) {
            failures.add(quality + " contrast ratio " + ratio + " below minimum " + minimum);
        }
    }

    private double contrastRatio(Color left, Color right) {
        var first = relativeLuminance(left);
        var second = relativeLuminance(right);
        var lighter = Math.max(first, second);
        var darker = Math.min(first, second);
        return (lighter + 0.05d) / (darker + 0.05d);
    }

    private double relativeLuminance(Color color) {
        return 0.2126d * linearize(color.getRed())
          + 0.7152d * linearize(color.getGreen())
          + 0.0722d * linearize(color.getBlue());
    }

    private double linearize(double channel) {
        if (channel <= 0.03928d) {
            return channel / 12.92d;
        }
        return Math.pow((channel + 0.055d) / 1.055d, 2.4d);
    }

    private Color compositeOnWhite(Color color, double alpha) {
        var red = color.getRed() * alpha + (1d - alpha);
        var green = color.getGreen() * alpha + (1d - alpha);
        var blue = color.getBlue() * alpha + (1d - alpha);
        return new Color(red, green, blue, 1d);
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
            public Result<StatusBarState> fetchStatusBar(Session session) {
                return statusBarResult.apply(session);
            }

            @Override
            public Result<CommonDashboard> fetchCommonDashboard() {
                return commonResult.get();
            }
        };
    }

    private StatusBarState statusBar(String signal, String networkType, String sim, String unread) {
        return new StatusBarState(signal, networkType, sim, unread);
    }
}
