package com.github.idelstak.routerfx.router.protocol;

import com.github.idelstak.routerfx.auth.login.*;
import com.github.idelstak.routerfx.dashboard.network.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;

public interface RouterApi extends ChallengePort, LoginPort, DashboardPort, CommonDashboardPort, StatusBarPort {

    @Override
    default Result<CommonDashboard> fetchCommonDashboard() {
        return new Result.Failure<>(new RouterFault.UnsupportedCommandFault("Common dashboard command set is unavailable"));
    }

    @Override
    default Result<StatusBarState> fetchStatusBar(Session session) {
        return new Result.Failure<>(new RouterFault.UnsupportedCommandFault("Status-bar command set is unavailable"));
    }
}
