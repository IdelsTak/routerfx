package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.shared.value.*;
import java.io.*;
import java.util.*;

sealed interface CliOutcome {

    void report(PrintStream out, PrintStream err);

    int code();

    record Ok(String baseUrl, RadioState radio) implements CliOutcome {

        public Ok {
            Objects.requireNonNull(baseUrl, "baseUrl must not be null");
            Objects.requireNonNull(radio, "radio must not be null");
        }

        @Override
        public void report(PrintStream out, PrintStream err) {
            Objects.requireNonNull(out, "out must not be null");
            Objects.requireNonNull(err, "err must not be null");
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

        @Override
        public int code() {
            return 0;
        }
    }

    record Failed(String message) implements CliOutcome {

        public Failed {
            Objects.requireNonNull(message, "message must not be null");
        }

        @Override
        public void report(PrintStream out, PrintStream err) {
            Objects.requireNonNull(out, "out must not be null");
            Objects.requireNonNull(err, "err must not be null");
            err.println(message);
        }

        @Override
        public int code() {
            return 1;
        }
    }
}
