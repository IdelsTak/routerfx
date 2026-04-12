package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;

final class LoginEffect implements Effect {

    private final RouterApiFactory apiFactory;

    LoginEffect(RouterApiFactory apiFactory) {
        this.apiFactory = Objects.requireNonNull(apiFactory, "apiFactory must not be null");
    }

    @Override
    public Optional<Msg> apply(AppState state, Msg msg) {
        return switch (msg) {
            case Msg.ConnectRequested(var baseUrl, var credentials) ->
              Optional.of(connect(baseUrl, credentials));
            case Msg.RefreshRequested _, Msg.Authenticated _, Msg.DashboardLoaded _, Msg.CommonLoaded _, Msg.Failed _ ->
              Optional.empty();
        };
    }

    private Msg connect(String baseUrl, Credentials credentials) {
        try {
            RouterApi api = apiFactory.create(baseUrl);
            Result<Msg> response = api.fetchChallenge()
              .flatMap(challenge -> api.login(credentials, challenge))
              .flatMap(session -> api.fetchRadioState(session).map(radio -> (Msg) new Msg.Authenticated(session, radio)));
            return message(response);
        } catch (RuntimeException issue) {
            return new Msg.Failed(new RouterFault.TransportFault(issue.getClass().getSimpleName()));
        }
    }

    private Msg message(Result<Msg> result) {
        return result.fold(message -> message, Msg.Failed::new);
    }
}
