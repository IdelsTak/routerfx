package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import com.github.idelstak.routerfx.shared.result.*;
import com.github.idelstak.routerfx.shared.value.*;
import java.util.*;

public final class FlowEffects implements Effect {

    private final RouterApiFactory apiFactory;

    public FlowEffects(RouterApiFactory apiFactory) {
        this.apiFactory = Objects.requireNonNull(apiFactory, "apiFactory must not be null");
    }

    @Override
    public Optional<Msg> apply(AppState state, Msg msg) {
        return switch (msg) {
            case Msg.ConnectRequested(var baseUrl, var credentials) ->
              Optional.of(connect(baseUrl, credentials));
            case Msg.RefreshRequested _ ->
              Optional.of(refresh(state));
            case Msg.Authenticated _, Msg.DashboardLoaded _, Msg.CommonLoaded _, Msg.Failed _ ->
              Optional.empty();
        };
    }

    private Msg connect(String baseUrl, Credentials credentials) {
        try {
            var api = apiFactory.create(baseUrl);
            var response = api.fetchChallenge()
              .flatMap(challenge -> api.login(credentials, challenge))
              .flatMap(session -> api.fetchRadioState(session).map(radio -> (Msg) new Msg.Authenticated(session, radio)));
            return message(response);
        } catch (RuntimeException issue) {
            return new Msg.Failed(new RouterFault.TransportFault(issue.getClass().getSimpleName()));
        }
    }

    private Msg refresh(AppState state) {
        return state.login().session()
          .map(session -> {
              try {
                  var api = apiFactory.create(state.login().baseUrl());
                  return message(api.fetchRadioState(session).map(radio -> (Msg) new Msg.DashboardLoaded(radio)));
              } catch (RuntimeException issue) {
                  return new Msg.Failed(new RouterFault.TransportFault(issue.getClass().getSimpleName()));
              }
          })
          .orElseGet(() -> common(state.login().baseUrl()));
    }

    private Msg common(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            return new Msg.Failed(new RouterFault.TransportFault("Router base URL is missing"));
        }
        try {
            var api = apiFactory.create(baseUrl);
            return message(api.fetchCommonDashboard().map(common -> (Msg) new Msg.CommonLoaded(common)));
        } catch (RuntimeException issue) {
            return new Msg.Failed(new RouterFault.TransportFault(issue.getClass().getSimpleName()));
        }
    }

    private Msg message(Result<Msg> result) {
        return result.fold(message -> message, Msg.Failed::new);
    }
}
