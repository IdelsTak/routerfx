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
}
