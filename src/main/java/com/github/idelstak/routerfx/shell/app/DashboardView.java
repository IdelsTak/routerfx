package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import javafx.beans.binding.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;

public final class DashboardView {

    private static final String NOTE_SUCCESS = "note-success";
    private static final String NOTE_WARNING = "note-warning";
    private static final String NOTE_CAUTION = "note-caution";
    private static final double RSRP_MIN = -140d;
    private static final double RSRP_MAX = -44d;
    private static final double RSRP_SWEEP = 180d;
    private static final String RSRP_TONE_NO_SIGNAL = "signal-gauge-tone-nosignal";
    private static final String RSRP_TONE_POOR = "signal-gauge-tone-poor";
    private static final String RSRP_TONE_FAIR = "signal-gauge-tone-fair";
    private static final String RSRP_TONE_GOOD = "signal-gauge-tone-good";
    private static final String RSRP_TONE_EXCELLENT = "signal-gauge-tone-excellent";
    private static final String CHIP_TONE_NO_SIGNAL = "signal-chip-tone-nosignal";
    private static final String CHIP_TONE_POOR = "signal-chip-tone-poor";
    private static final String CHIP_TONE_FAIR = "signal-chip-tone-fair";
    private static final String CHIP_TONE_GOOD = "signal-chip-tone-good";
    private static final String CHIP_TONE_EXCELLENT = "signal-chip-tone-excellent";
    private static final String METRIC_CHIP_TONE_NO_SIGNAL = "metric-chip-tone-nosignal";
    private static final String METRIC_CHIP_TONE_POOR = "metric-chip-tone-poor";
    private static final String METRIC_CHIP_TONE_FAIR = "metric-chip-tone-fair";
    private static final String METRIC_CHIP_TONE_GOOD = "metric-chip-tone-good";
    private static final String METRIC_CHIP_TONE_EXCELLENT = "metric-chip-tone-excellent";
    private static final String METRIC_RANGE_TONE_NO_SIGNAL = "metric-range-fill-nosignal";
    private static final String METRIC_RANGE_TONE_POOR = "metric-range-fill-poor";
    private static final String METRIC_RANGE_TONE_FAIR = "metric-range-fill-fair";
    private static final String METRIC_RANGE_TONE_GOOD = "metric-range-fill-good";
    private static final String METRIC_RANGE_TONE_EXCELLENT = "metric-range-fill-excellent";
    private final FxStore fxStore;
    @FXML
    private BorderPane shellRoot;
    @FXML
    private VBox topStatusCard;
    @FXML
    private VBox signalCard;
    @FXML
    private VBox networkPathCard;
    @FXML
    private VBox footerStatsCard;
    @FXML
    private TextField baseUrl;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button connect;
    @FXML
    private Button refresh;
    @FXML
    private Button loginSubmit;
    @FXML
    private Button loginCancel;
    @FXML
    private Label noteLabel;
    @FXML
    private VBox loginOverlay;
    @FXML
    private VBox authenticated;
    private Label routerPathNode;
    private Label internetPathNode;
    private Label primaryDnsPathNode;
    private Label secondaryDnsPathNode;
    private Label primaryDnsLink;
    private Label secondaryDnsLink;
    private Label networkTypeValue;
    private Label simValue;
    private Label atValue;
    private Label wifi24Value;
    private Label wifi5Value;
    private Label lanValue;
    private Label rsrpValue;
    private HBox signalChip;
    private Label signalChipText;
    private Label signalHeroCaption;
    private Arc rsrpGaugeFill;
    private Label rssiValue;
    private Label rssiQualityChip;
    private AnchorPane rssiRangeTrack;
    private Region rssiRangeFill;
    private Label rsrqValue;
    private Label rsrqQualityChip;
    private AnchorPane rsrqRangeTrack;
    private Region rsrqRangeFill;
    private Label sinrValue;
    private Label sinrQualityChip;
    private AnchorPane sinrRangeTrack;
    private Region sinrRangeFill;
    private Label pciMirrorValue;
    private Label earfcnMirrorValue;
    private Label ipValue;
    private Label wanMacValue;
    private Label primaryDnsValue;
    private Label secondaryDnsValue;
    private Label ipv6Value;
    private Label primaryIpv6DnsValue;
    private Label secondaryIpv6DnsValue;
    private Label runningTimeValue;
    private Label firmwareVersionValue;
    private Label antennaStatusValue;
    @FXML
    private Label operatorValue;
    @FXML
    private Label currentBandValue;
    @FXML
    private Label bandwidthValue;
    @FXML
    private Label uptimeValue;
    @FXML
    private Label currentFlowValue;
    @FXML
    private Label statusSignalValue;
    @FXML
    private Label statusNetworkTypeValue;
    @FXML
    private Label statusSimValue;
    @FXML
    private Label statusSmsValue;

    public DashboardView(FxStore fxStore) {
        this.fxStore = Objects.requireNonNull(fxStore, "fxStore must not be null");
    }

    @FXML
    protected void initialize() {
        resolveIncludedNodes();
        bindValues();
        wire();
    }

    private void resolveIncludedNodes() {
        networkTypeValue = requiredLabel(topStatusCard, "networkTypeValue");
        simValue = requiredLabel(topStatusCard, "simValue");
        atValue = requiredLabel(topStatusCard, "atValue");
        wifi24Value = requiredLabel(topStatusCard, "wifi24Value");
        wifi5Value = requiredLabel(topStatusCard, "wifi5Value");
        lanValue = requiredLabel(topStatusCard, "lanValue");
        rsrpValue = requiredLabel(signalCard, "rsrpValue");
        signalChip = requiredHBox(signalCard, "signalChip");
        signalChipText = requiredLabel(signalCard, "signalChipText");
        signalHeroCaption = requiredLabel(signalCard, "signalHeroCaption");
        rsrpGaugeFill = requiredArc(signalCard, "rsrpGaugeFill");
        rssiValue = requiredLabel(signalCard, "rssiValue");
        rssiQualityChip = requiredLabel(signalCard, "rssiQualityChip");
        rssiRangeTrack = requiredAnchorPane(signalCard, "rssiRangeTrack");
        rssiRangeFill = requiredRegion(signalCard, "rssiRangeFill");
        rsrqValue = requiredLabel(signalCard, "rsrqValue");
        rsrqQualityChip = requiredLabel(signalCard, "rsrqQualityChip");
        rsrqRangeTrack = requiredAnchorPane(signalCard, "rsrqRangeTrack");
        rsrqRangeFill = requiredRegion(signalCard, "rsrqRangeFill");
        sinrValue = requiredLabel(signalCard, "sinrValue");
        sinrQualityChip = requiredLabel(signalCard, "sinrQualityChip");
        sinrRangeTrack = requiredAnchorPane(signalCard, "sinrRangeTrack");
        sinrRangeFill = requiredRegion(signalCard, "sinrRangeFill");
        pciMirrorValue = requiredLabel(networkPathCard, "pciMirrorValue");
        earfcnMirrorValue = requiredLabel(networkPathCard, "earfcnMirrorValue");
        ipValue = requiredLabel(networkPathCard, "ipValue");
        wanMacValue = requiredLabel(networkPathCard, "wanMacValue");
        primaryDnsValue = requiredLabel(networkPathCard, "primaryDnsValue");
        secondaryDnsValue = requiredLabel(networkPathCard, "secondaryDnsValue");
        ipv6Value = requiredLabel(networkPathCard, "ipv6Value");
        primaryIpv6DnsValue = requiredLabel(networkPathCard, "primaryIpv6DnsValue");
        secondaryIpv6DnsValue = requiredLabel(networkPathCard, "secondaryIpv6DnsValue");
        runningTimeValue = requiredLabel(footerStatsCard, "runningTimeValue");
        firmwareVersionValue = requiredLabel(footerStatsCard, "firmwareVersionValue");
        antennaStatusValue = requiredLabel(footerStatsCard, "antennaStatusValue");
        operatorValue = requiredLabel(authenticated, "operatorValue");
        currentBandValue = requiredLabel(authenticated, "currentBandValue");
        bandwidthValue = requiredLabel(authenticated, "bandwidthValue");
        uptimeValue = requiredLabel(authenticated, "uptimeValue");
        currentFlowValue = requiredLabel(authenticated, "currentFlowValue");
        statusSignalValue = requiredLabel(authenticated, "statusSignalValue");
        statusNetworkTypeValue = requiredLabel(authenticated, "statusNetworkTypeValue");
        statusSimValue = requiredLabel(authenticated, "statusSimValue");
        statusSmsValue = requiredLabel(authenticated, "statusSmsValue");
        routerPathNode = requiredLabel(networkPathCard, "routerPathNode");
        internetPathNode = requiredLabel(networkPathCard, "internetPathNode");
        primaryDnsPathNode = requiredLabel(networkPathCard, "primaryDnsPathNode");
        secondaryDnsPathNode = requiredLabel(networkPathCard, "secondaryDnsPathNode");
        primaryDnsLink = requiredLabel(networkPathCard, "primaryDnsLink");
        secondaryDnsLink = requiredLabel(networkPathCard, "secondaryDnsLink");
    }

    private void bindValues() {
        bindCommon(networkTypeValue, CommonDashboard::networkType);
        bindCommon(simValue, CommonDashboard::sim);
        bindCommon(atValue, CommonDashboard::at);
        bindCommon(wifi24Value, CommonDashboard::wifi24);
        bindCommon(wifi5Value, CommonDashboard::wifi5);
        bindCommon(lanValue, CommonDashboard::lan);

        bindCommonMetricValue(rsrpValue, CommonDashboard::rsrp);
        bindCommonMetricValue(rssiValue, CommonDashboard::rssi);
        bindCommonMetricValue(rsrqValue, CommonDashboard::rsrq);
        bindCommonMetricValue(sinrValue, CommonDashboard::sinr);
        bindCommon(pciMirrorValue, CommonDashboard::pci);
        bindCommon(earfcnMirrorValue, CommonDashboard::earfcn);

        bindCommon(ipValue, CommonDashboard::ip);
        bindCommon(wanMacValue, CommonDashboard::wanMac);
        bindCommon(primaryDnsValue, CommonDashboard::primaryDns);
        bindCommon(secondaryDnsValue, CommonDashboard::secondaryDns);
        bindCommon(ipv6Value, CommonDashboard::ipv6);
        bindCommon(primaryIpv6DnsValue, CommonDashboard::primaryIpv6Dns);
        bindCommon(secondaryIpv6DnsValue, CommonDashboard::secondaryIpv6Dns);

        bindCommon(runningTimeValue, CommonDashboard::runningTime);
        bindCommon(firmwareVersionValue, CommonDashboard::firmwareVersion);
        bindCommon(antennaStatusValue, CommonDashboard::antennaStatus);

        bindRadio(operatorValue, RadioState::networkOperator);
        bindRadio(currentBandValue, RadioState::currentBand);
        bindRadio(bandwidthValue, RadioState::bandwidth);
        bindRadio(uptimeValue, RadioState::onlineDuration);
        bindRadio(currentFlowValue, item -> item.flowDl() + "/" + item.flowUl());

        bindStatusBar(statusSignalValue, StatusBarState::signalLevel);
        bindStatusBar(statusNetworkTypeValue, StatusBarState::networkType);
        bindStatusBar(statusSimValue, StatusBarState::sim);
        bindStatusBar(statusSmsValue, StatusBarState::smsUnread);
        rsrpValue.textProperty().addListener((_, _, value) -> applyRsrpGauge(value));
        rssiValue.textProperty().addListener((_, _, value) -> applyRssiQuality(value));
        sinrValue.textProperty().addListener((_, _, value) -> applySinrQuality(value));
        rsrqValue.textProperty().addListener((_, _, value) -> applyRsrqQuality(value));
        applyRsrpGauge(rsrpValue.getText());
        applyRssiQuality(rssiValue.getText());
        applySinrQuality(sinrValue.getText());
        applyRsrqQuality(rsrqValue.getText());
        rssiRangeTrack.widthProperty().addListener((_, _, __) -> applyRssiQuality(rssiValue.getText()));
        sinrRangeTrack.widthProperty().addListener((_, _, __) -> applySinrQuality(sinrValue.getText()));
        rsrqRangeTrack.widthProperty().addListener((_, _, __) -> applyRsrqQuality(rsrqValue.getText()));
    }

    private void bindCommon(Label label, Function<CommonDashboard, String> projector) {
        label.textProperty().bind(stringValue(state ->
          state.dashboard().common().map(projector).orElse("-")));
    }

    private void bindCommonMetricValue(Label label, Function<CommonDashboard, String> projector) {
        label.textProperty().bind(stringValue(state ->
          state.dashboard().common().map(projector).map(this::formatDualMetricValue).orElse("-/-")));
    }

    private void bindRadio(Label label, Function<RadioState, String> projector) {
        label.textProperty().bind(stringValue(state ->
          state.dashboard().radio().map(projector).orElse("-")));
    }

    private void bindStatusBar(Label label, Function<StatusBarState, String> projector) {
        label.textProperty().bind(stringValue(state ->
          state.dashboard().statusBar().map(projector).orElse("-")));
    }

    private void wire() {
        noteLabel.textProperty().bind(stringValue(state -> state.ui().note()));
        applyNoteTone(fxStore.read().ui().note());
        loginOverlay.visibleProperty().bind(booleanValue(state -> state.ui().loginOverlayVisible()));
        loginOverlay.managedProperty().bind(loginOverlay.visibleProperty());

        syncLoginFields(fxStore.read());
        fxStore.stateProperty().addListener((_, _, newValue) -> {
            syncLoginFields(newValue);
            applyNoteTone(newValue.ui().note());
            applyPathTone(newValue);
        });
        applyPathTone(fxStore.read());

        connect.setOnAction(_ -> fxStore.dispatch(new Msg.LoginOverlayOpened()));
        loginCancel.setOnAction(_ -> fxStore.dispatch(new Msg.LoginOverlayClosed()));
        loginSubmit.setOnAction(_ -> {
            fxStore.dispatch(new Msg.ConnectRequested(
              baseUrl.getText(),
              new Credentials(username.getText(), password.getText())
            ));
            password.clear();
        });
        refresh.setOnAction(_ -> fxStore.dispatch(new Msg.RefreshRequested()));
        loginSubmit.disableProperty().bind(booleanValue(state -> state.ui().busy()));
        refresh.disableProperty().bind(booleanValue(state ->
          !state.ui().canRefresh() || state.ui().busy()));

        var authVisible = booleanValue(state ->
          state.login().session().isPresent() && state.dashboard().radio().isPresent());
        authenticated.visibleProperty().bind(authVisible);
        authenticated.managedProperty().bind(authVisible);
    }

    private void syncLoginFields(AppState state) {
        var nextBaseUrl = state.login().baseUrl();
        if (!baseUrl.isFocused() && !baseUrl.getText().equals(nextBaseUrl)) {
            baseUrl.setText(nextBaseUrl);
        }
        var nextUsername = state.login().username();
        if (!username.isFocused() && !username.getText().equals(nextUsername)) {
            username.setText(nextUsername);
        }
    }

    private StringBinding stringValue(Function<AppState, String> projector) {
        return Bindings.createStringBinding(() ->
          safe(projector.apply(fxStore.stateProperty().get())), fxStore.stateProperty());
    }

    private BooleanBinding booleanValue(Function<AppState, Boolean> projector) {
        return Bindings.createBooleanBinding(() -> projector.apply(fxStore.stateProperty().get()), fxStore.stateProperty());
    }

    private Label requiredLabel(Parent root, String id) {
        return findById(root, id)
          .filter(Label.class::isInstance)
          .map(Label.class::cast)
          .orElseThrow(() -> new IllegalStateException("Missing label: " + id));
    }

    private Arc requiredArc(Parent root, String id) {
        return findById(root, id)
          .filter(Arc.class::isInstance)
          .map(Arc.class::cast)
          .orElseThrow(() -> new IllegalStateException("Missing arc: " + id));
    }

    private HBox requiredHBox(Parent root, String id) {
        return findById(root, id)
          .filter(HBox.class::isInstance)
          .map(HBox.class::cast)
          .orElseThrow(() -> new IllegalStateException("Missing hbox: " + id));
    }

    private AnchorPane requiredAnchorPane(Parent root, String id) {
        return findById(root, id)
          .filter(AnchorPane.class::isInstance)
          .map(AnchorPane.class::cast)
          .orElseThrow(() -> new IllegalStateException("Missing anchor pane: " + id));
    }

    private Region requiredRegion(Parent root, String id) {
        return findById(root, id)
          .filter(Region.class::isInstance)
          .map(Region.class::cast)
          .orElseThrow(() -> new IllegalStateException("Missing region: " + id));
    }

    private Optional<Node> findById(Node node, String id) {
        if (id.equals(node.getId())) {
            return Optional.of(node);
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                var match = findById(child, id);
                if (match.isPresent()) {
                    return match;
                }
            }
        }
        return Optional.empty();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String formatDualMetricValue(String value) {
        if (value == null || value.isBlank()) {
            return "-/-";
        }
        var parts = value.split("/", -1);
        var fourG = normalizeMetricPart(parts.length > 0 ? parts[0] : "-");
        var fiveG = normalizeMetricPart(parts.length > 1 ? parts[1] : "-");
        return fourG + "/" + fiveG;
    }

    private String normalizeMetricPart(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        var normalized = value.trim()
          .replaceAll("(?i)\\s*dBm\\s*$", "")
          .replaceAll("(?i)\\s*dB\\s*$", "")
          .trim();
        return normalized.isBlank() ? "-" : normalized;
    }

    private void applyRsrpGauge(String text) {
        if (rsrpGaugeFill == null) {
            return;
        }
        parseRsrp(text)
          .ifPresentOrElse(
            value -> {
              applyRsrpFillLength(value);
              applyRsrpTone(value);
              applySignalChipTone(value);
              applySignalChipText(value);
              applySignalHeroCaption(value);
          },
            () -> {
              applyRsrpFillLength(RSRP_MIN);
              applyRsrpTone(RSRP_MIN);
              applySignalChipTone(RSRP_MIN);
              applySignalChipText(RSRP_MIN);
              applySignalHeroCaption(RSRP_MIN);
          }
          );
    }

    private Optional<Double> parseRsrp(String text) {
        return parsePrimaryMetric(text);
    }

    private Optional<Double> parsePrimaryMetric(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }
        var primary = text.split("/", 2)[0];
        var normalized = primary.replace('\u2212', '-');
        var matcher = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(normalized);
        if (!matcher.find()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.valueOf(matcher.group()));
        } catch (NumberFormatException issue) {
            return Optional.empty();
        }
    }

    private void applyRsrpFillLength(double value) {
        var clamped = Math.max(RSRP_MIN, Math.min(RSRP_MAX, value));
        var ratio = (clamped - RSRP_MIN) / (RSRP_MAX - RSRP_MIN);
        var constrainedRatio = Math.max(0d, Math.min(1d, ratio));
        rsrpGaugeFill.setLength(-1d * constrainedRatio * RSRP_SWEEP);
    }

    private void applyRsrpTone(double value) {
        String tone = rsrpToneClass(value);
        applyRsrpTone(rsrpGaugeFill, tone);
    }

    private void applyRsrpTone(Arc zone, String tone) {
        zone.getStyleClass().removeAll(
          RSRP_TONE_NO_SIGNAL,
          RSRP_TONE_POOR,
          RSRP_TONE_FAIR,
          RSRP_TONE_GOOD,
          RSRP_TONE_EXCELLENT
        );
        zone.getStyleClass().add(tone);
    }

    private String rsrpToneClass(double value) {
        if (value <= -110d) {
            return RSRP_TONE_NO_SIGNAL;
        }
        if (value <= -100d) {
            return RSRP_TONE_POOR;
        }
        if (value <= -90d) {
            return RSRP_TONE_FAIR;
        }
        if (value <= -80d) {
            return RSRP_TONE_GOOD;
        }
        return RSRP_TONE_EXCELLENT;
    }

    private void applySignalHeroCaption(double value) {
        if (signalHeroCaption == null) {
            return;
        }
        signalHeroCaption.setText(rsrpDescription(value));
    }

    private void applySignalChipText(double value) {
        if (signalChipText == null) {
            return;
        }
        signalChipText.setText(rsrpQuality(value));
    }

    private void applySignalChipTone(double value) {
        if (signalChip == null) {
            return;
        }
        var tone = rsrpChipToneClass(value);
        signalChip.getStyleClass().removeAll(
          CHIP_TONE_NO_SIGNAL,
          CHIP_TONE_POOR,
          CHIP_TONE_FAIR,
          CHIP_TONE_GOOD,
          CHIP_TONE_EXCELLENT
        );
        signalChip.getStyleClass().add(tone);
    }

    private String rsrpQuality(double value) {
        if (value <= -110d) {
            return "No signal";
        }
        if (value <= -100d) {
            return "Poor";
        }
        if (value <= -90d) {
            return "Fair";
        }
        if (value <= -80d) {
            return "Good";
        }
        return "Excellent";
    }

    private String rsrpChipToneClass(double value) {
        if (value <= -110d) {
            return CHIP_TONE_NO_SIGNAL;
        }
        if (value <= -100d) {
            return CHIP_TONE_POOR;
        }
        if (value <= -90d) {
            return CHIP_TONE_FAIR;
        }
        if (value <= -80d) {
            return CHIP_TONE_GOOD;
        }
        return CHIP_TONE_EXCELLENT;
    }

    private String rsrpDescription(double value) {
        if (value <= -110d) {
            return "Connection unusable or dropped";
        }
        if (value <= -100d) {
            return "Drops likely, very slow speeds";
        }
        if (value <= -90d) {
            return "Usable but inconsistent";
        }
        if (value <= -80d) {
            return "Reliable for most tasks";
        }
        return "Strong, stable, fast speeds";
    }

    private void applyRssiQuality(String text) {
        var quality = parsePrimaryMetric(text)
          .map(this::rssiQualityBand)
          .orElse(new MetricQuality("No signal", METRIC_CHIP_TONE_NO_SIGNAL, METRIC_RANGE_TONE_NO_SIGNAL, 0d));
        applyMetricVisuals(rssiQualityChip, quality, rssiRangeFill, rssiRangeTrack);
    }

    private void applySinrQuality(String text) {
        var quality = parsePrimaryMetric(text)
          .map(this::sinrQualityBand)
          .orElse(new MetricQuality("No signal", METRIC_CHIP_TONE_NO_SIGNAL, METRIC_RANGE_TONE_NO_SIGNAL, 0d));
        applyMetricVisuals(sinrQualityChip, quality, sinrRangeFill, sinrRangeTrack);
    }

    private void applyRsrqQuality(String text) {
        var quality = parsePrimaryMetric(text)
          .map(this::rsrqQualityBand)
          .orElse(new MetricQuality("No signal", METRIC_CHIP_TONE_NO_SIGNAL, METRIC_RANGE_TONE_NO_SIGNAL, 0d));
        applyMetricVisuals(rsrqQualityChip, quality, rsrqRangeFill, rsrqRangeTrack);
    }

    private void applyMetricVisuals(Label chip, MetricQuality quality, Region rangeFill, AnchorPane rangeTrack) {
        applyMetricChipQuality(chip, quality);
        applyMetricRangeQuality(rangeFill, rangeTrack, quality);
    }

    private void applyMetricChipQuality(Label chip, MetricQuality quality) {
        if (chip == null) {
            return;
        }
        chip.setText(quality.label());
        chip.getStyleClass().removeAll(
          METRIC_CHIP_TONE_NO_SIGNAL,
          METRIC_CHIP_TONE_POOR,
          METRIC_CHIP_TONE_FAIR,
          METRIC_CHIP_TONE_GOOD,
          METRIC_CHIP_TONE_EXCELLENT
        );
        chip.getStyleClass().add(quality.chipToneClass());
    }

    private void applyMetricRangeQuality(Region rangeFill, AnchorPane rangeTrack, MetricQuality quality) {
        if (rangeFill == null || rangeTrack == null) {
            return;
        }
        rangeFill.getStyleClass().removeAll(
          METRIC_RANGE_TONE_NO_SIGNAL,
          METRIC_RANGE_TONE_POOR,
          METRIC_RANGE_TONE_FAIR,
          METRIC_RANGE_TONE_GOOD,
          METRIC_RANGE_TONE_EXCELLENT
        );
        rangeFill.getStyleClass().add(quality.rangeToneClass());
        var trackWidth = rangeTrack.getWidth();
        var trackHeight = rangeTrack.getHeight();
        if (trackWidth <= 0d || trackHeight <= 0d) {
            return;
        }
        var fillWidth = trackWidth * quality.ratio();
        rangeFill.setMinSize(fillWidth, trackHeight);
        rangeFill.setPrefSize(fillWidth, trackHeight);
        rangeFill.setMaxSize(fillWidth, trackHeight);
    }

    private MetricQuality rssiQualityBand(double value) {
        var ratio = normalizedRatio(value, -110d, -50d);
        if (value <= -110d) {
            return new MetricQuality("No signal", METRIC_CHIP_TONE_NO_SIGNAL, METRIC_RANGE_TONE_NO_SIGNAL, ratio);
        }
        if (value <= -100d) {
            return new MetricQuality("Poor", METRIC_CHIP_TONE_POOR, METRIC_RANGE_TONE_POOR, ratio);
        }
        if (value <= -90d) {
            return new MetricQuality("Fair", METRIC_CHIP_TONE_FAIR, METRIC_RANGE_TONE_FAIR, ratio);
        }
        if (value <= -80d) {
            return new MetricQuality("Good", METRIC_CHIP_TONE_GOOD, METRIC_RANGE_TONE_GOOD, ratio);
        }
        return new MetricQuality("Excellent", METRIC_CHIP_TONE_EXCELLENT, METRIC_RANGE_TONE_EXCELLENT, ratio);
    }

    private MetricQuality sinrQualityBand(double value) {
        var ratio = normalizedRatio(value, -3d, 20d);
        if (value <= -3d) {
            return new MetricQuality("No signal", METRIC_CHIP_TONE_NO_SIGNAL, METRIC_RANGE_TONE_NO_SIGNAL, ratio);
        }
        if (value <= 0d) {
            return new MetricQuality("Poor", METRIC_CHIP_TONE_POOR, METRIC_RANGE_TONE_POOR, ratio);
        }
        if (value <= 10d) {
            return new MetricQuality("Fair", METRIC_CHIP_TONE_FAIR, METRIC_RANGE_TONE_FAIR, ratio);
        }
        if (value <= 20d) {
            return new MetricQuality("Good", METRIC_CHIP_TONE_GOOD, METRIC_RANGE_TONE_GOOD, ratio);
        }
        return new MetricQuality("Excellent", METRIC_CHIP_TONE_EXCELLENT, METRIC_RANGE_TONE_EXCELLENT, ratio);
    }

    private MetricQuality rsrqQualityBand(double value) {
        var ratio = normalizedRatio(value, -20d, -5d);
        if (value <= -20d) {
            return new MetricQuality("No signal", METRIC_CHIP_TONE_NO_SIGNAL, METRIC_RANGE_TONE_NO_SIGNAL, ratio);
        }
        if (value <= -15d) {
            return new MetricQuality("Poor", METRIC_CHIP_TONE_POOR, METRIC_RANGE_TONE_POOR, ratio);
        }
        if (value <= -10d) {
            return new MetricQuality("Fair", METRIC_CHIP_TONE_FAIR, METRIC_RANGE_TONE_FAIR, ratio);
        }
        if (value <= -5d) {
            return new MetricQuality("Good", METRIC_CHIP_TONE_GOOD, METRIC_RANGE_TONE_GOOD, ratio);
        }
        return new MetricQuality("Excellent", METRIC_CHIP_TONE_EXCELLENT, METRIC_RANGE_TONE_EXCELLENT, ratio);
    }

    private double normalizedRatio(double value, double min, double max) {
        if (max <= min) {
            return 0d;
        }
        var clamped = Math.max(min, Math.min(max, value));
        var ratio = (clamped - min) / (max - min);
        return Math.max(0d, Math.min(1d, ratio));
    }

    private void applyNoteTone(String note) {
        noteLabel.getStyleClass().removeAll(NOTE_SUCCESS, NOTE_WARNING, NOTE_CAUTION);
        if (note == null || note.isBlank()) {
            noteLabel.getStyleClass().add(NOTE_SUCCESS);
            return;
        }
        var lower = note.toLowerCase(Locale.ROOT);
        if (lower.contains("fail")) {
            noteLabel.getStyleClass().add(NOTE_CAUTION);
            return;
        }
        if (lower.contains("expired")) {
            noteLabel.getStyleClass().add(NOTE_WARNING);
            return;
        }
        noteLabel.getStyleClass().add(NOTE_SUCCESS);
    }

    private void applyPathTone(AppState state) {
        var common = state.dashboard().common();
        var ip = common.map(CommonDashboard::ip).orElse("-");
        var primary = common.map(CommonDashboard::primaryDns).orElse("-");
        var secondary = common.map(CommonDashboard::secondaryDns).orElse("-");
        var routerUp = !"-".equals(ip);
        var primaryUp = !"-".equals(primary);
        var secondaryUp = !"-".equals(secondary);
        tone(routerPathNode, routerUp ? "path-node-active" : "path-node-inactive");
        tone(internetPathNode, routerUp ? "path-node-active" : "path-node-inactive");
        tone(primaryDnsPathNode, primaryUp ? "path-node-active" : "path-node-inactive");
        tone(secondaryDnsPathNode, secondaryUp ? "path-node-active" : "path-node-fallback");
        tone(primaryDnsLink, primaryUp ? "path-link-active" : "path-link-inactive");
        tone(secondaryDnsLink, secondaryUp ? "path-link-active" : "path-link-fallback");
    }

    private void tone(Labeled node, String tone) {
        node.getStyleClass().removeAll(
          "path-node-active",
          "path-node-inactive",
          "path-node-fallback",
          "path-link-active",
          "path-link-inactive",
          "path-link-fallback"
        );
        node.getStyleClass().add(tone);
    }

    private record MetricQuality(String label, String chipToneClass, String rangeToneClass, double ratio) {

    }
}
