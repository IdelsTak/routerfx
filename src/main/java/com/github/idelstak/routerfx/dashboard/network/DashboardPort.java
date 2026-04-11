package com.github.idelstak.routerfx.dashboard.network;

import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;

public interface DashboardPort {

    Result<RadioState> fetchRadioState(Session session);
}
