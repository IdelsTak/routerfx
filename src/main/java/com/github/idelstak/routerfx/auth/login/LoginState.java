package com.github.idelstak.routerfx.auth.login;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;

public record LoginState(String baseUrl, String username, Optional<Session> session, Optional<RouterFault> fault) {

    public LoginState {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(session, "session must not be null");
        Objects.requireNonNull(fault, "fault must not be null");
    }
}
