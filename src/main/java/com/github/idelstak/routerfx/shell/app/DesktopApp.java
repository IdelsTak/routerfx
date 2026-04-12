package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

public final class DesktopApp extends Application {

    private final RouterApiFactory factory;
    private final Executor executor;

    public DesktopApp() {
        this(new DefaultRouterApiFactory(), command -> Thread.ofVirtual().start(command));
    }

    DesktopApp(RouterApiFactory factory, Executor executor) {
        this.factory = factory;
        this.executor = executor;
    }

    @Override
    public void start(Stage stage) {
        AppShell shell = new AppShell(factory, executor);
        FxStore fxStore = new FxStore(shell.store());
        DashboardPane pane = new DashboardPane(fxStore);
        stage.setTitle("RouterFX");
        stage.setScene(new Scene(pane.root(), 780, 860));
        stage.show();
        fxStore.dispatch(new Msg.RefreshRequested());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
