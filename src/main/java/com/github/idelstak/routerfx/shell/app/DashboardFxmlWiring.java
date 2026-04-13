package com.github.idelstak.routerfx.shell.app;

import java.util.*;
import javafx.util.*;

final class DashboardFxmlWiring implements Callback<Class<?>, Object> {

    private final FxStore fxStore;

    DashboardFxmlWiring(FxStore fxStore) {
        this.fxStore = Objects.requireNonNull(fxStore, "fxStore must not be null");
    }

    @Override
    public Object call(Class<?> type) {
        if (type == DashboardView.class) {
            return new DashboardView(fxStore);
        }
        throw new IllegalStateException("Unsupported controller type: " + type.getName());
    }
}
