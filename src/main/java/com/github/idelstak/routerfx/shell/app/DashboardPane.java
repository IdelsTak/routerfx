package com.github.idelstak.routerfx.shell.app;

import java.io.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public final class DashboardPane {

    private final BorderPane root;

    public DashboardPane(FxStore fxStore) {
        root = view(fxStore);
    }

    public Parent root() {
        return root;
    }

    private BorderPane view(FxStore fxStore) {
        try {
            var loader = new FXMLLoader(getClass().getResource("dashboard-pane.fxml"));
            loader.setControllerFactory(new DashboardFxmlWiring(fxStore));
            return loader.load();
        } catch (IOException issue) {
            throw new IllegalStateException("Cannot load dashboard FXML", issue);
        }
    }
}
