package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.shared.value.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public final class DashboardPane {

    private final Store store;
    private final BorderPane root;
    private final TextField baseUrl;
    private final TextField username;
    private final PasswordField password;
    private final Button connect;
    private final Button refresh;
    private final Label note;
    private final Label networkType;
    private final Label sim;
    private final Label at;
    private final Label wifi24;
    private final Label wifi5;
    private final Label lan;
    private final Label rsrp;
    private final Label rssi;
    private final Label rsrq;
    private final Label sinr;
    private final Label pci;
    private final Label earfcn;
    private final Label ip;
    private final Label wanMac;
    private final Label primaryDns;
    private final Label secondaryDns;
    private final Label ipv6;
    private final Label primaryIpv6Dns;
    private final Label secondaryIpv6Dns;
    private final Label runningTime;
    private final Label firmwareVersion;
    private final Label antennaStatus;
    private final VBox authenticated;
    private final Label operator;
    private final Label currentBand;
    private final Label bandwidth;
    private final Label uptime;
    private final Label currentFlow;

    public DashboardPane(Store store) {
        this.store = store;
        this.baseUrl = new TextField();
        this.username = new TextField();
        this.password = new PasswordField();
        this.connect = new Button("Connect");
        this.refresh = new Button("Refresh");
        this.note = new Label("-");
        this.networkType = new Label("-");
        this.sim = new Label("-");
        this.at = new Label("-");
        this.wifi24 = new Label("-");
        this.wifi5 = new Label("-");
        this.lan = new Label("-");
        this.rsrp = new Label("-");
        this.rssi = new Label("-");
        this.rsrq = new Label("-");
        this.sinr = new Label("-");
        this.pci = new Label("-");
        this.earfcn = new Label("-");
        this.ip = new Label("-");
        this.wanMac = new Label("-");
        this.primaryDns = new Label("-");
        this.secondaryDns = new Label("-");
        this.ipv6 = new Label("-");
        this.primaryIpv6Dns = new Label("-");
        this.secondaryIpv6Dns = new Label("-");
        this.runningTime = new Label("-");
        this.firmwareVersion = new Label("-");
        this.antennaStatus = new Label("-");
        this.authenticated = new VBox(8);
        this.operator = new Label("-");
        this.currentBand = new Label("-");
        this.bandwidth = new Label("-");
        this.uptime = new Label("-");
        this.currentFlow = new Label("-");
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
        note.setId("noteLabel");
        networkType.setId("networkTypeValue");
        sim.setId("simValue");
        at.setId("atValue");
        wifi24.setId("wifi24Value");
        wifi5.setId("wifi5Value");
        lan.setId("lanValue");
        rsrp.setId("rsrpValue");
        rssi.setId("rssiValue");
        rsrq.setId("rsrqValue");
        sinr.setId("sinrValue");
        pci.setId("pciValue");
        earfcn.setId("earfcnValue");
        ip.setId("ipValue");
        wanMac.setId("wanMacValue");
        primaryDns.setId("primaryDnsValue");
        secondaryDns.setId("secondaryDnsValue");
        ipv6.setId("ipv6Value");
        primaryIpv6Dns.setId("primaryIpv6DnsValue");
        secondaryIpv6Dns.setId("secondaryIpv6DnsValue");
        runningTime.setId("runningTimeValue");
        firmwareVersion.setId("firmwareVersionValue");
        antennaStatus.setId("antennaStatusValue");
        authenticated.setId("authPanel");
        operator.setId("operatorValue");
        currentBand.setId("currentBandValue");
        bandwidth.setId("bandwidthValue");
        uptime.setId("uptimeValue");
        currentFlow.setId("currentFlowValue");
        baseUrl.setText("http://192.168.1.1");
        username.setText("admin");

        var login = new GridPane();
        login.setHgap(8);
        login.setVgap(8);
        login.add(new Label("Router URL"), 0, 0);
        login.add(baseUrl, 1, 0);
        login.add(new Label("Username"), 0, 1);
        login.add(username, 1, 1);
        login.add(new Label("Password"), 0, 2);
        login.add(password, 1, 2);
        var actions = new HBox(8, connect, refresh);
        login.add(actions, 1, 3);

        var common = new GridPane();
        common.setHgap(8);
        common.setVgap(6);
        row(common, 0, "Network Type", networkType);
        row(common, 1, "SIM", sim);
        row(common, 2, "AT", at);
        row(common, 3, "2.4 GHz Wi-Fi", wifi24);
        row(common, 4, "5 GHz Wi-Fi", wifi5);
        row(common, 5, "LAN", lan);
        row(common, 6, "RSRP", rsrp);
        row(common, 7, "RSSI", rssi);
        row(common, 8, "RSRQ", rsrq);
        row(common, 9, "SINR", sinr);
        row(common, 10, "PCI", pci);
        row(common, 11, "EARFCN", earfcn);
        row(common, 12, "IP", ip);
        row(common, 13, "WAN MAC", wanMac);
        row(common, 14, "Primary DNS", primaryDns);
        row(common, 15, "Secondary DNS", secondaryDns);
        row(common, 16, "IPv6", ipv6);
        row(common, 17, "Primary IPv6 DNS", primaryIpv6Dns);
        row(common, 18, "Secondary IPv6 DNS", secondaryIpv6Dns);
        row(common, 19, "Running Time", runningTime);
        row(common, 20, "Firmware Version", firmwareVersion);
        row(common, 21, "Antenna Status", antennaStatus);

        var auth = new GridPane();
        auth.setHgap(8);
        auth.setVgap(6);
        row(auth, 0, "Operator", operator);
        row(auth, 1, "Current Band", currentBand);
        row(auth, 2, "Bandwidth", bandwidth);
        row(auth, 3, "Uptime", uptime);
        row(auth, 4, "Current Traffic", currentFlow);
        authenticated.getChildren().addAll(new Label("Authenticated Dashboard"), auth);
        authenticated.setManaged(false);
        authenticated.setVisible(false);

        var content = new VBox(12, new Label("RouterFX"), login, note, new Label("Common Dashboard"), common, authenticated);
        content.setPadding(new Insets(12));
        var pane = new BorderPane(content);
        BorderPane.setMargin(content, new Insets(8));
        return pane;
    }

    private void row(GridPane grid, int index, String title, Label value) {
        grid.add(new Label(title), 0, index);
        grid.add(value, 1, index);
    }

    private void wire() {
        connect.setOnAction(event -> {
            store.dispatch(new Msg.ConnectRequested(baseUrl.getText(), new Credentials(username.getText(), password.getText())));
            password.clear();
        });
        refresh.setOnAction(event -> store.dispatch(new Msg.RefreshRequested()));
        store.watch(state -> Platform.runLater(() -> render(state)));
    }

    private void render(AppState state) {
        baseUrl.setText(state.login().baseUrl());
        username.setText(state.login().username());
        connect.setDisable(state.ui().busy());
        refresh.setDisable(!state.ui().canRefresh() || state.ui().busy());
        note.setText(state.ui().note());
        state.dashboard().common().ifPresent(this::common);
        var show = state.login().session().isPresent() && state.dashboard().radio().isPresent();
        authenticated.setManaged(show);
        authenticated.setVisible(show);
        state.dashboard().radio().ifPresent(this::radio);
    }

    private void common(CommonDashboard common) {
        networkType.setText(common.networkType());
        sim.setText(common.sim());
        at.setText(common.at());
        wifi24.setText(common.wifi24());
        wifi5.setText(common.wifi5());
        lan.setText(common.lan());
        rsrp.setText(common.rsrp());
        rssi.setText(common.rssi());
        rsrq.setText(common.rsrq());
        sinr.setText(common.sinr());
        pci.setText(common.pci());
        earfcn.setText(common.earfcn());
        ip.setText(common.ip());
        wanMac.setText(common.wanMac());
        primaryDns.setText(common.primaryDns());
        secondaryDns.setText(common.secondaryDns());
        ipv6.setText(common.ipv6());
        primaryIpv6Dns.setText(common.primaryIpv6Dns());
        secondaryIpv6Dns.setText(common.secondaryIpv6Dns());
        runningTime.setText(common.runningTime());
        firmwareVersion.setText(common.firmwareVersion());
        antennaStatus.setText(common.antennaStatus());
    }

    private void radio(RadioState radio) {
        operator.setText(radio.networkOperator());
        currentBand.setText(radio.currentBand());
        bandwidth.setText(radio.bandwidth());
        uptime.setText(radio.onlineDuration());
        currentFlow.setText(radio.flowDl() + "/" + radio.flowUl());
    }
}
