package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.databind.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;

final class RouterProtocolResponses {

    Result<Challenge> challenge(JsonNode node) {
        return required(node, "token").map(Challenge::new);
    }

    Result<Session> session(JsonNode node) {
        if ("fail".equals(node.path("login_fail").asText()) || "fail".equals(node.path("login_fail2").asText())) {
            return new Result.Failure<>(new RouterFault.AuthFault("Login rejected by router"));
        }
        return required(node, "sessionId").map(Session::new);
    }

    Result<RadioState> radio(JsonNode node) {
        return required(node, "network_operator").flatMap(networkOperator ->
                required(node, "network_type_str").flatMap(networkTypeStr ->
                required(node, "RSRP").flatMap(rsrp ->
                required(node, "RSSI").flatMap(rssi ->
                required(node, "RSRQ").flatMap(rsrq ->
                required(node, "SINR").flatMap(sinr ->
                required(node, "currentband").flatMap(currentBand ->
                required(node, "bandwidth").flatMap(bandwidth ->
                required(node, "flow_dl").flatMap(flowDl ->
                required(node, "flow_ul").flatMap(flowUl ->
                required(node, "onlineTime").flatMap(onlineTime ->
                required(node, "onlineDuration").map(onlineDuration ->
                    new RadioState(
                            networkOperator,
                            networkTypeStr,
                            rsrp,
                            rssi,
                            rsrq,
                            sinr,
                            currentBand,
                            bandwidth,
                            flowDl,
                            flowUl,
                            onlineTime,
                            onlineDuration
                    )))))))))))));
    }

    Result<CommonDashboard> common(JsonNode initPage, JsonNode sysStatus, JsonNode routerInfo) {
        return new Result.Success<>(new CommonDashboard(
          text(sysStatus, "network_type_str"),
          sim(sysStatus),
          "AT",
          wifi(text(sysStatus, "wlan2g_switch_0"), "2.4 GHz Wi-Fi"),
          wifi(text(sysStatus, "wlan5g_switch_0"), "5 GHz Wi-Fi"),
          lan(sysStatus),
          pair(routerInfo, "RSRP", "RSRP_5G", "dBm"),
          pair(routerInfo, "RSSI", "RSSI_5G", "dBm"),
          pair(routerInfo, "RSRQ", "RSRQ_5G", "dB"),
          pair(routerInfo, "SINR", "SINR_5G", "dB"),
          pair(routerInfo, "PCI", "PCI_5G", ""),
          pair(routerInfo, "FREQ", "FREQ_5G", ""),
          text(routerInfo, "wan_ip"),
          mac(text(routerInfo, "wan_mac")),
          text(routerInfo, "wan_dns"),
          text(routerInfo, "wan_dns2"),
          text(routerInfo, "wan_ipv6_ip"),
          text(routerInfo, "wan_ipv6_dns"),
          text(routerInfo, "wan_ipv6_dns2"),
          uptime(routerInfo),
          text(routerInfo, "fake_version"),
          antenna(initPage)
        ));
    }

    private Result<String> required(JsonNode node, String fieldName) {
        var value = node.get(fieldName);
        if (value == null || value.isNull()) {
            return new Result.Failure<>(new RouterFault.MalformedResponseFault("Missing required field: " + fieldName));
        }
        var text = value.asText();
        if (text == null || text.isBlank()) {
            return new Result.Failure<>(new RouterFault.MalformedResponseFault("Blank required field: " + fieldName));
        }
        return new Result.Success<>(text);
    }

    private String text(JsonNode node, String fieldName) {
        var value = node.get(fieldName);
        if (value == null || value.isNull()) {
            return "-";
        }
        var text = value.asText();
        return text == null || text.isBlank() ? "-" : text;
    }

    private String pair(JsonNode node, String left, String right, String unit) {
        var first = unit(text(node, left), unit);
        var second = unit(text(node, right), unit);
        return first + "/" + second;
    }

    private String unit(String value, String unit) {
        if ("-".equals(value) || unit.isBlank()) {
            return value;
        }
        return value + unit;
    }

    private String mac(String value) {
        if ("-".equals(value)) {
            return value;
        }
        var compact = value.replace(":", "").replace("-", "").toUpperCase();
        if (compact.length() != 12) {
            return value;
        }
        return compact.substring(0, 2) + ":" + compact.substring(2, 4) + ":" + compact.substring(4, 6) + ":" + compact.substring(6, 8) + ":" + compact.substring(8, 10) + ":" + compact.substring(10, 12);
    }

    private String uptime(JsonNode node) {
        var value = text(node, "uptime");
        if ("-".equals(value)) {
            return value;
        }
        try {
            var seconds = Long.parseLong(value);
            var hours = seconds / 3600;
            var minutes = (seconds % 3600) / 60;
            var remains = seconds % 60;
            return two(hours) + ":" + two(minutes) + ":" + two(remains);
        } catch (NumberFormatException issue) {
            return value;
        }
    }

    private String two(long value) {
        if (value < 10) {
            return "0" + value;
        }
        return Long.toString(value);
    }

    private String antenna(JsonNode node) {
        return switch (text(node, "antenna_status")) {
            case "all" -> "All Direction";
            case "front" -> "Front Direction";
            case "back" -> "Back Direction";
            default -> "-";
        };
    }

    private String sim(JsonNode node) {
        return "1".equals(text(node, "sim_status")) ? "SIM" : "No SIM";
    }

    private String wifi(String enabled, String label) {
        return "1".equals(enabled) ? label : "-";
    }

    private String lan(JsonNode node) {
        var links = node.get("wired_link_list");
        if (links == null || !links.isArray() || links.isEmpty()) {
            return "-";
        }
        var first = links.get(0).asText("-");
        return first == null || first.isBlank() ? "-" : first;
    }
}
