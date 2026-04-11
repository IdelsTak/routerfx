package com.github.idelstak.routerfx.router.protocol;

public final class DefaultRouterApiFactory implements RouterApiFactory {

    @Override
    public RouterApi create(String baseUrl) {
        return new HttpRouterApi(baseUrl);
    }
}
