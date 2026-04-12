package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.auth.login.*;
import com.github.idelstak.routerfx.dashboard.network.*;
import java.util.*;

public record AppState(LoginState login, DashboardState dashboard, UiState ui) {

    public AppState {
        Objects.requireNonNull(login, "login must not be null");
        Objects.requireNonNull(dashboard, "dashboard must not be null");
        Objects.requireNonNull(ui, "ui must not be null");
    }

    public static AppState initial() {
        return new AppState(
          new LoginState("", "", Optional.empty(), Optional.empty()),
          new DashboardState(Optional.empty(), Optional.empty(), false, 0),
          new UiState(false, false, "Idle", false)
        );
    }
}
