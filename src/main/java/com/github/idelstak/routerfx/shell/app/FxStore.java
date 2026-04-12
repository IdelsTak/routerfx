package com.github.idelstak.routerfx.shell.app;

import javafx.application.*;
import javafx.beans.property.*;
import java.util.*;

public final class FxStore {

    private final Store store;
    private final ReadOnlyObjectWrapper<AppState> state;

    public FxStore(Store store) {
        this.store = Objects.requireNonNull(store, "store must not be null");
        this.state = new ReadOnlyObjectWrapper<>(store.read());
        store.watch(snapshot -> Platform.runLater(() -> state.set(snapshot)));
    }

    public void dispatch(Msg msg) {
        store.dispatch(msg);
    }

    public ReadOnlyObjectProperty<AppState> stateProperty() {
        return state.getReadOnlyProperty();
    }

    public AppState read() {
        return state.get();
    }
}
