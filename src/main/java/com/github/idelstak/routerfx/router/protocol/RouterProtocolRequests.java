package com.github.idelstak.routerfx.router.protocol;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import java.util.*;

final class RouterProtocolRequests {

    private final ObjectMapper mapper;
    private final String language;

    RouterProtocolRequests(ObjectMapper mapper, String language) {
        this.mapper = Objects.requireNonNull(mapper, "mapper must not be null");
        this.language = Objects.requireNonNull(language, "language must not be null");
    }

    ObjectNode challenge() {
        var request = mapper.createObjectNode();
        request.put("cmd", 232);
        request.put("method", "GET");
        request.put("sessionId", "");
        request.put("language", language);
        return request;
    }

    ObjectNode login(String username, String passwd, String sessionId) {
        var request = mapper.createObjectNode();
        request.put("cmd", 100);
        request.put("method", "POST");
        request.put("sessionId", sessionId);
        request.put("username", username);
        request.put("passwd", passwd);
        request.put("isAutoUpgrade", "0");
        request.put("language", language);
        return request;
    }

    ObjectNode radio(String sessionId) {
        var request = mapper.createObjectNode();
        request.put("cmd", 205);
        request.put("method", "GET");
        request.put("language", language);
        request.put("sessionId", sessionId);
        return request;
    }
}
