package com.github.idelstak.routerfx.auth.login;

import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;

public interface LoginPort {

    Result<Session> login(Credentials credentials, Challenge challenge);
}
