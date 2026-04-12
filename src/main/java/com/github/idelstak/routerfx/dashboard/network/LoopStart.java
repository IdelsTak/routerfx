package com.github.idelstak.routerfx.dashboard.network;

import java.time.*;

interface LoopStart {

    RefreshLoop begin(Duration wait, Runnable tick);
}
