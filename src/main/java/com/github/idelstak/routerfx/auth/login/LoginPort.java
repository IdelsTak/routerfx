package com.github.idelstak.routerfx.auth.login;

import com.github.idelstak.routerfx.shared.value.*;

public interface LoginPort {

    Session login(Credentials credentials, Challenge challenge);
}
