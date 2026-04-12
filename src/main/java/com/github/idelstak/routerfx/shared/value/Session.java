package com.github.idelstak.routerfx.shared.value;

import java.util.*;

public record Session(SessionId sessionId) {

    public Session {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
    }

    public Session(String sessionId) {
        this(new SessionId(sessionId));
    }
}
