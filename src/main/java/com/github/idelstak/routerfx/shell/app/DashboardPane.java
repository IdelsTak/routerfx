package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.shared.value.CommonDashboard;
import com.github.idelstak.routerfx.shared.value.Credentials;
import com.github.idelstak.routerfx.shared.value.RadioState;
import java.util.Optional;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class DashboardPane {

    private final FxStore fxStore;
    private final BorderPane root;
    private final TextField baseUrl;
    private final TextField username;
    private final PasswordField password;
    private final Button connect;
    private final Button refresh;
    private final VBox authenticated;

    public DashboardPane(FxStore fxStore) {
        this.fxStore = fxStore;
        this.baseUrl = new TextField();
        this.username = new TextField();
        this.password = new PasswordField();
        this.connect = new Button("Connect");
        this.refresh = new Button("Refresh");
        this.authenticated = new VBox(8);
        this.root = layout();
        wire();
    }

    public Parent root() {
        return root;
    }

    private BorderPane layout() {
        baseUrl.setId("baseUrlField");
        username.setId("usernameField");
        password.setId("passwordField");
        connect.setId("connectButton");
        refresh.setId("refreshButton");

        GridPane login = new GridPane(8, 8);
        login.add(new Label("Router URL"), 0, 0);
        login.add(baseUrl, 1, 0);
        login.add(new Label("Username"), 0, 1);
        login.add(username, 1, 1);
        login.add(new Label("Password"), 0, 2);
        login.add(password, 1, 2);
        login.add(new HBox(8, connect, refresh), 1, 3);

        Label note = value("noteLabel", state -> state.ui().note());

        GridPane commonGrid = new GridPane(8, 6);
        row(commonGrid, 0, "Network Type", value("networkTypeValue", state -> commonValue(state.dashboard().common(), CommonDashboard::networkType)));
        row(commonGrid, 1, "SIM", value("simValue", state -> commonValue(state.dashboard().common(), CommonDashboard::sim)));
        row(commonGrid, 2, "AT", value("atValue", state -> commonValue(state.dashboard().common(), CommonDashboard::at)));
        row(commonGrid, 3, "2.4 GHz Wi-Fi", value("wifi24Value", state -> commonValue(state.dashboard().common(), CommonDashboard::wifi24)));
        row(commonGrid, 4, "5 GHz Wi-Fi", value("wifi5Value", state -> commonValue(state.dashboard().common(), CommonDashboard::wifi5)));
        row(commonGrid, 5, "LAN", value("lanValue", state -> commonValue(state.dashboard().common(), CommonDashboard::lan)));
        row(commonGrid, 6, "RSRP", value("rsrpValue", state -> commonValue(state.dashboard().common(), CommonDashboard::rsrp)));
        row(commonGrid, 7, "RSSI", value("rssiValue", state -> commonValue(state.dashboard().common(), CommonDashboard::rssi)));
        row(commonGrid, 8, "RSRQ", value("rsrqValue", state -> commonValue(state.dashboard().common(), CommonDashboard::rsrq)));
        row(commonGrid, 9, "SINR", value("sinrValue", state -> commonValue(state.dashboard().common(), CommonDashboard::sinr)));
        row(commonGrid, 10, "PCI", value("pciValue", state -> commonValue(state.dashboard().common(), CommonDashboard::pci)));
        row(commonGrid, 11, "EARFCN", value("earfcnValue", state -> commonValue(state.dashboard().common(), CommonDashboard::earfcn)));
        row(commonGrid, 12, "IP", value("ipValue", state -> commonValue(state.dashboard().common(), CommonDashboard::ip)));
        row(commonGrid, 13, "WAN MAC", value("wanMacValue", state -> commonValue(state.dashboard().common(), CommonDashboard::wanMac)));
        row(commonGrid, 14, "Primary DNS", value("primaryDnsValue", state -> commonValue(state.dashboard().common(), CommonDashboard::primaryDns)));
        row(commonGrid, 15, "Secondary DNS", value("secondaryDnsValue", state -> commonValue(state.dashboard().common(), CommonDashboard::secondaryDns)));
        row(commonGrid, 16, "IPv6", value("ipv6Value", state -> commonValue(state.dashboard().common(), CommonDashboard::ipv6)));
        row(commonGrid, 17, "Primary IPv6 DNS", value("primaryIpv6DnsValue", state -> commonValue(state.dashboard().common(), CommonDashboard::primaryIpv6Dns)));
        row(commonGrid, 18, "Secondary IPv6 DNS", value("secondaryIpv6DnsValue", state -> commonValue(state.dashboard().common(), CommonDashboard::secondaryIpv6Dns)));
        row(commonGrid, 19, "Running Time", value("runningTimeValue", state -> commonValue(state.dashboard().common(), CommonDashboard::runningTime)));
        row(commonGrid, 20, "Firmware Version", value("firmwareVersionValue", state -> commonValue(state.dashboard().common(), CommonDashboard::firmwareVersion)));
        row(commonGrid, 21, "Antenna Status", value("antennaStatusValue", state -> commonValue(state.dashboard().common(), CommonDashboard::antennaStatus)));

        authenticated.setId("authPanel");
        GridPane authGrid = new GridPane(8, 6);
        row(authGrid, 0, "Operator", value("operatorValue", state -> radioValue(state, RadioState::networkOperator)));
        row(authGrid, 1, "Current Band", value("currentBandValue", state -> radioValue(state, RadioState::currentBand)));
        row(authGrid, 2, "Bandwidth", value("bandwidthValue", state -> radioValue(state, RadioState::bandwidth)));
        row(authGrid, 3, "Uptime", value("uptimeValue", state -> radioValue(state, RadioState::onlineDuration)));
        row(authGrid, 4, "Current Traffic", value("currentFlowValue", state -> radioValue(state, item -> item.flowDl() + "/" + item.flowUl())));
        authenticated.getChildren().addAll(new Label("Authenticated Dashboard"), authGrid);

        VBox content = new VBox(12, new Label("RouterFX"), login, note, new Label("Common Dashboard"), commonGrid, authenticated);
        content.setPadding(new javafx.geometry.Insets(12));
        return new BorderPane(content);
    }

    private void wire() {
        syncLoginFields(fxStore.read());
        fxStore.stateProperty().addListener((stateObserver, oldValue, newValue) -> syncLoginFields(newValue));

        connect.setOnAction(event -> {
            fxStore.dispatch(new Msg.ConnectRequested(
              baseUrl.getText(),
              new Credentials(username.getText(), password.getText())
            ));
            password.clear();
        });
        refresh.setOnAction(event -> fxStore.dispatch(new Msg.RefreshRequested()));
        connect.disableProperty().bind(booleanValue(state -> state.ui().busy()));
        refresh.disableProperty().bind(booleanValue(state -> !state.ui().canRefresh() || state.ui().busy()));

        BooleanBinding authVisible = booleanValue(state -> state.login().session().isPresent() && state.dashboard().radio().isPresent());
        authenticated.visibleProperty().bind(authVisible);
        authenticated.managedProperty().bind(authVisible);
    }

    private void syncLoginFields(AppState state) {
        String nextBaseUrl = state.login().baseUrl();
        if (!baseUrl.isFocused() && !baseUrl.getText().equals(nextBaseUrl)) {
            baseUrl.setText(nextBaseUrl);
        }

        String nextUsername = state.login().username();
        if (!username.isFocused() && !username.getText().equals(nextUsername)) {
            username.setText(nextUsername);
        }
    }

    private Label value(String id, Function<AppState, String> projector) {
        Label label = new Label("-");
        label.setId(id);
        label.textProperty().bind(stringValue(projector));
        return label;
    }

    private StringBinding stringValue(Function<AppState, String> projector) {
        return Bindings.createStringBinding(() -> safe(projector.apply(fxStore.stateProperty().get())), fxStore.stateProperty());
    }

    private BooleanBinding booleanValue(Function<AppState, Boolean> projector) {
        return Bindings.createBooleanBinding(() -> projector.apply(fxStore.stateProperty().get()), fxStore.stateProperty());
    }

    private void row(GridPane grid, int index, String title, Labeled value) {
        grid.add(new Label(title), 0, index);
        grid.add(value, 1, index);
    }

    private String commonValue(Optional<CommonDashboard> common, Function<CommonDashboard, String> projector) {
        return common.map(projector).orElse("-");
    }

    private String radioValue(AppState state, Function<RadioState, String> projector) {
        return state.dashboard().radio().map(projector).orElse("-");
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
