package com.github.idelstak.routerfx.shell.app;

public interface Update {

    AppState apply(AppState state, Msg msg);
}
