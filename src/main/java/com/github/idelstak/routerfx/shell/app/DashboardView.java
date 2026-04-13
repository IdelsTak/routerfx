package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;
import java.util.function.*;
import javafx.beans.binding.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public final class DashboardView {

    private static final String NOTE_SUCCESS = "note-success";
    private static final String NOTE_WARNING = "note-warning";
    private static final String NOTE_CAUTION = "note-caution";
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
    private Label rssiValue;
    private Label rsrqValue;
    private Label sinrValue;
    private Label pciValue;
    private Label earfcnValue;
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
        rssiValue = requiredLabel(signalCard, "rssiValue");
        rsrqValue = requiredLabel(signalCard, "rsrqValue");
        sinrValue = requiredLabel(signalCard, "sinrValue");
        pciValue = requiredLabel(signalCard, "pciValue");
        earfcnValue = requiredLabel(signalCard, "earfcnValue");
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

        bindCommon(rsrpValue, CommonDashboard::rsrp);
        bindCommon(rssiValue, CommonDashboard::rssi);
        bindCommon(rsrqValue, CommonDashboard::rsrq);
        bindCommon(sinrValue, CommonDashboard::sinr);
        bindCommon(pciValue, CommonDashboard::pci);
        bindCommon(earfcnValue, CommonDashboard::earfcn);

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
    }

    private void bindCommon(Label label, Function<CommonDashboard, String> projector) {
        label.textProperty().bind(stringValue(state ->
          state.dashboard().common().map(projector).orElse("-")));
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
}
