package com.github.idelstak.routerfx.dashboard.network;

import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;

public interface StatusBarPort {

    Result<StatusBarState> fetchStatusBar(Session session);
}
