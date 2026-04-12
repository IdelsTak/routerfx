package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.auth.login.*;
import com.github.idelstak.routerfx.dashboard.network.*;
import com.github.idelstak.routerfx.router.protocol.*;
import java.util.*;
import java.util.function.*;

public final class FlowEffects implements Effect {

    private final List<Effect> effects;

    public FlowEffects(RouterApiFactory apiFactory) {
        this(
          apiFactory,
          new PeriodicRefreshEffect()
        );
    }

    FlowEffects(RouterApiFactory apiFactory, Effect periodicRefreshEffect) {
        Objects.requireNonNull(apiFactory, "apiFactory must not be null");
        this.effects = List.of(
          Objects.requireNonNull(periodicRefreshEffect, "periodicRefreshEffect must not be null"),
          new LoginEffect(apiFactory),
          new RefreshEffect(apiFactory)
        );
    }

    @Override
    public Optional<Msg> apply(AppState state, Msg msg) {
        for (Effect effect : effects) {
            Optional<Msg> result = effect.apply(state, msg);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    @Override
    public void attach(Consumer<Msg> dispatch) {
        for (Effect effect : effects) {
            effect.attach(dispatch);
        }
    }
}
