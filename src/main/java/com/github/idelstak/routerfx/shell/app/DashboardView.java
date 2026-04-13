package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;
import java.util.function.*;
import javafx.beans.binding.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public final class DashboardView {

    private static final String NOTE_SUCCESS = "note-success";
    private static final String NOTE_WARNING = "note-warning";
    private static final String NOTE_CAUTION = "note-caution";
    private final FxStore fxStore;
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
    private Label noteLabel;
    @FXML
    private VBox authenticated;
    @FXML
    private GridPane commonGrid;
    @FXML
    private GridPane authGrid;
    @FXML
    private GridPane statusGrid;

    public DashboardView(FxStore fxStore) {
        this.fxStore = Objects.requireNonNull(fxStore, "fxStore must not be null");
    }

    @FXML
    protected void initialize() {
        rows();
        wire();
    }

    private void rows() {
        row(commonGrid, 0, "Network Type", value("networkTypeValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::networkType)));
        row(commonGrid, 1, "SIM", value("simValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::sim)));
        row(commonGrid, 2, "AT", value("atValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::at)));
        row(commonGrid, 3, "2.4 GHz Wi-Fi", value("wifi24Value", state ->
          commonValue(state.dashboard().common(), CommonDashboard::wifi24)));
        row(commonGrid, 4, "5 GHz Wi-Fi", value("wifi5Value", state ->
          commonValue(state.dashboard().common(), CommonDashboard::wifi5)));
        row(commonGrid, 5, "LAN", value("lanValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::lan)));
        row(commonGrid, 6, "RSRP", value("rsrpValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::rsrp), "metric-caution"));
        row(commonGrid, 7, "RSSI", value("rssiValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::rssi), "metric-caution"));
        row(commonGrid, 8, "RSRQ", value("rsrqValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::rsrq), "metric-warning"));
        row(commonGrid, 9, "SINR", value("sinrValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::sinr), "metric-warning"));
        row(commonGrid, 10, "PCI", value("pciValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::pci)));
        row(commonGrid, 11, "EARFCN", value("earfcnValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::earfcn)));
        row(commonGrid, 12, "IP", value("ipValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::ip)));
        row(commonGrid, 13, "WAN MAC", value("wanMacValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::wanMac)));
        row(commonGrid, 14, "Primary DNS", value("primaryDnsValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::primaryDns)));
        row(commonGrid, 15, "Secondary DNS", value("secondaryDnsValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::secondaryDns)));
        row(commonGrid, 16, "IPv6", value("ipv6Value", state ->
          commonValue(state.dashboard().common(), CommonDashboard::ipv6)));
        row(commonGrid, 17, "Primary IPv6 DNS", value("primaryIpv6DnsValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::primaryIpv6Dns)));
        row(commonGrid, 18, "Secondary IPv6 DNS", value("secondaryIpv6DnsValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::secondaryIpv6Dns)));
        row(commonGrid, 19, "Running Time", value("runningTimeValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::runningTime)));
        row(commonGrid, 20, "Firmware Version", value("firmwareVersionValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::firmwareVersion)));
        row(commonGrid, 21, "Antenna Status", value("antennaStatusValue", state ->
          commonValue(state.dashboard().common(), CommonDashboard::antennaStatus)));

        row(authGrid, 0, "Operator", value("operatorValue", state ->
          radioValue(state, RadioState::networkOperator)));
        row(authGrid, 1, "Current Band", value("currentBandValue", state ->
          radioValue(state, RadioState::currentBand)));
        row(authGrid, 2, "Bandwidth", value("bandwidthValue", state ->
          radioValue(state, RadioState::bandwidth)));
        row(authGrid, 3, "Uptime", value("uptimeValue", state ->
          radioValue(state, RadioState::onlineDuration)));
        row(authGrid, 4, "Current Traffic", value("currentFlowValue", state ->
          radioValue(state, item -> item.flowDl() + "/" + item.flowUl())));

        row(statusGrid, 0, "Signal Level", value("statusSignalValue", state ->
          statusBarValue(state, StatusBarState::signalLevel), "metric-success"));
        row(statusGrid, 1, "Network Type", value("statusNetworkTypeValue", state ->
          statusBarValue(state, StatusBarState::networkType)));
        row(statusGrid, 2, "SIM", value("statusSimValue", state ->
          statusBarValue(state, StatusBarState::sim)));
        row(statusGrid, 3, "SMS Unread", value("statusSmsValue", state ->
          statusBarValue(state, StatusBarState::smsUnread)));
    }

    private void wire() {
        noteLabel.textProperty().bind(stringValue(state -> state.ui().note()));
        applyNoteTone(fxStore.read().ui().note());

        syncLoginFields(fxStore.read());
        fxStore.stateProperty().addListener((_, _, newValue) -> {
            syncLoginFields(newValue);
            applyNoteTone(newValue.ui().note());
        });

        connect.setOnAction(_ -> {
            fxStore.dispatch(new Msg.ConnectRequested(
              baseUrl.getText(),
              new Credentials(username.getText(), password.getText())
            ));
            password.clear();
        });
        refresh.setOnAction(_ -> fxStore.dispatch(new Msg.RefreshRequested()));
        connect.disableProperty().bind(booleanValue(state -> state.ui().busy()));
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

    private Label value(String id, Function<AppState, String> projector) {
        var label = new Label("-");
        label.setId(id);
        label.getStyleClass().add("metric-value");
        label.textProperty().bind(stringValue(projector));
        return label;
    }

    private Label value(String id, Function<AppState, String> projector, String tone) {
        var label = value(id, projector);
        label.getStyleClass().add(tone);
        return label;
    }

    private StringBinding stringValue(Function<AppState, String> projector) {
        return Bindings.createStringBinding(() ->
          safe(projector.apply(fxStore.stateProperty().get())), fxStore.stateProperty());
    }

    private BooleanBinding booleanValue(Function<AppState, Boolean> projector) {
        return Bindings.createBooleanBinding(() -> projector.apply(fxStore.stateProperty().get()), fxStore.stateProperty());
    }

    private void row(GridPane grid, int index, String title, Labeled value) {
        var label = new Label(title);
        label.getStyleClass().add("metric-label");
        grid.add(label, 0, index);
        grid.add(value, 1, index);
    }

    private String commonValue(Optional<CommonDashboard> common, Function<CommonDashboard, String> projector) {
        return common.map(projector).orElse("-");
    }

    private String radioValue(AppState state, Function<RadioState, String> projector) {
        return state.dashboard().radio().map(projector).orElse("-");
    }

    private String statusBarValue(AppState state, Function<StatusBarState, String> projector) {
        return state.dashboard().statusBar().map(projector).orElse("-");
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
}
