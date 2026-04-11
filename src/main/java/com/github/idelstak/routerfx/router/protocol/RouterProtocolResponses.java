package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.databind.*;
import com.github.idelstak.routerfx.shared.value.*;

final class RouterProtocolResponses {

    Challenge toChallenge(JsonNode node) {
        return new Challenge(requiredText(node, "token"));
    }

    Session toSession(JsonNode node) {
        if ("fail".equals(node.path("login_fail").asText()) || "fail".equals(node.path("login_fail2").asText())) {
            throw new RouterProtocolException("Login rejected: " + node);
        }
        return new Session(requiredText(node, "sessionId"));
    }

    RadioState toRadioState(JsonNode node) {
        return new RadioState(
          requiredText(node, "network_operator"),
          requiredText(node, "network_type_str"),
          requiredText(node, "RSRP"),
          requiredText(node, "RSSI"),
          requiredText(node, "RSRQ"),
          requiredText(node, "SINR"),
          requiredText(node, "currentband"),
          requiredText(node, "bandwidth"),
          requiredText(node, "flow_dl"),
          requiredText(node, "flow_ul"),
          requiredText(node, "onlineTime"),
          requiredText(node, "onlineDuration")
        );
    }

    private String requiredText(JsonNode node, String fieldName) {
        var value = node.get(fieldName);
        if (value == null || value.isNull()) {
            throw new RouterProtocolException("Missing required field: " + fieldName + " in " + node);
        }
        var text = value.asText();
        if (text == null || text.isBlank()) {
            throw new RouterProtocolException("Blank required field: " + fieldName + " in " + node);
        }
        return text;
    }
}
