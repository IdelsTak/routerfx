package com.github.idelstak.routerfx.auth.login;

import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;

public interface ChallengePort {

    Result<Challenge> fetchChallenge();
}
