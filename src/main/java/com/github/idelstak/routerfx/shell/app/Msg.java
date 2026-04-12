package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;

public sealed interface Msg {

    record ConnectRequested(String baseUrl, Credentials credentials) implements Msg {

        public ConnectRequested {
            Objects.requireNonNull(baseUrl, "baseUrl must not be null");
            Objects.requireNonNull(credentials, "credentials must not be null");
        }
    }

    record RefreshRequested() implements Msg {
    }

    record Authenticated(Session session, RadioState radio, StatusBarState statusBar) implements Msg {

        public Authenticated {
            Objects.requireNonNull(session, "session must not be null");
            Objects.requireNonNull(radio, "radio must not be null");
            Objects.requireNonNull(statusBar, "statusBar must not be null");
        }
    }

    record DashboardLoaded(RadioState radio, StatusBarState statusBar) implements Msg {

        public DashboardLoaded {
            Objects.requireNonNull(radio, "radio must not be null");
            Objects.requireNonNull(statusBar, "statusBar must not be null");
        }
    }

    record CommonLoaded(CommonDashboard common) implements Msg {

        public CommonLoaded {
            Objects.requireNonNull(common, "common must not be null");
        }
    }

    record Failed(RouterFault fault) implements Msg {

        public Failed {
            Objects.requireNonNull(fault, "fault must not be null");
        }
    }
}
