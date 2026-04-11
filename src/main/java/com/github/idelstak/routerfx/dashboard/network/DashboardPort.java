package com.github.idelstak.routerfx.dashboard.network;

import com.github.idelstak.routerfx.shared.value.*;

public interface DashboardPort {

    RadioState fetchRadioState(Session session);
}
