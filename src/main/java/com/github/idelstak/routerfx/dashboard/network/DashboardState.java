package com.github.idelstak.routerfx.dashboard.network;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;

public record DashboardState(Optional<CommonDashboard> common, Optional<RadioState> radio, Optional<StatusBarState> statusBar, Optional<RouterFault> fault, boolean refreshing, int updates) {

    public DashboardState {
        Objects.requireNonNull(common, "common must not be null");
        Objects.requireNonNull(radio, "radio must not be null");
        Objects.requireNonNull(statusBar, "statusBar must not be null");
        Objects.requireNonNull(fault, "fault must not be null");
    }
}
