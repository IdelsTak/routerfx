package com.github.idelstak.routerfx.dashboard.network;

final class NoopLoop implements RefreshLoop {

    @Override
    public void cancel() {
    }

    @Override
    public boolean active() {
        return false;
    }
}
