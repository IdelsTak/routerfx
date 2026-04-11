package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.io.*;
import java.util.*;

public final class ProofCli {

    private final PasswordInput passwordInput;
    private final RouterApiFactory apiFactory;
    private final PrintStream out;
    private final PrintStream err;

    public ProofCli(PasswordInput passwordInput, RouterApiFactory apiFactory, PrintStream out, PrintStream err) {
        this.passwordInput = Objects.requireNonNull(passwordInput, "passwordInput must not be null");
        this.apiFactory = Objects.requireNonNull(apiFactory, "apiFactory must not be null");
        this.out = Objects.requireNonNull(out, "out must not be null");
        this.err = Objects.requireNonNull(err, "err must not be null");
    }

    public int run(String[] args) {
        Objects.requireNonNull(args, "args must not be null");
        if (args.length == 3) {
            err.println("Password must not be provided as a command argument");
            err.println("Usage: java ... <routerBaseUrl> <username>");
            err.println("Password will be requested securely at runtime");
            return 2;
        }
        if (args.length != 2) {
            err.println("Usage: java ... <routerBaseUrl> <username>");
            err.println("Example: http://192.168.1.1 admin");
            err.println("Password will be requested securely at runtime");
            return 2;
        }
        var baseUrl = args[0];
        var username = args[1];
        var password = passwordInput.read(username, err);
        if (password == null || password.length == 0) {
            err.println("Failed: Empty password");
            return 2;
        }
        try {
            var api = apiFactory.create(baseUrl);
            var credentials = new Credentials(username, new String(password));
            var challenge = api.fetchChallenge();
            var session = api.login(credentials, challenge);
            var radio = api.fetchRadioState(session);
            printSummary(baseUrl, radio);
            return 0;
        } catch (Exception e) {
            err.println("Failed: Router operation failed");
            return 1;
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private void printSummary(String baseUrl, RadioState radio) {
        out.println("Airtel Router Radio Status");
        out.println("Router          : " + baseUrl);
        out.println("Operator        : " + radio.networkOperator());
        out.println("Network Type    : " + radio.networkTypeStr());
        out.println("RSRP            : " + radio.rsrp());
        out.println("RSSI            : " + radio.rssi());
        out.println("RSRQ            : " + radio.rsrq());
        out.println("SINR            : " + radio.sinr());
        out.println("Current Band    : " + radio.currentBand());
        out.println("Bandwidth       : " + radio.bandwidth());
        out.println("Downlink Flow   : " + radio.flowDl());
        out.println("Uplink Flow     : " + radio.flowUl());
        out.println("Online Time     : " + radio.onlineTime());
        out.println("Online Duration : " + radio.onlineDuration());
    }
}
