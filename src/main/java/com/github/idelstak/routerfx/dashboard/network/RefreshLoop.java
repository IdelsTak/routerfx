package com.github.idelstak.routerfx.dashboard.network;

interface RefreshLoop {

    void cancel();

    boolean active();
}
