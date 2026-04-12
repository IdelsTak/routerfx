package com.github.idelstak.routerfx.shell.app;

import com.github.idelstak.routerfx.router.protocol.*;
import java.util.*;

public final class FlowEffects implements Effect {

    private final List<Effect> effects;

    public FlowEffects(RouterApiFactory apiFactory) {
        Objects.requireNonNull(apiFactory, "apiFactory must not be null");
        this.effects = List.of(
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
}
