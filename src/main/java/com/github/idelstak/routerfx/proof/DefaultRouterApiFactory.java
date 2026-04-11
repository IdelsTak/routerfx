package com.github.idelstak.routerfx.proof;

public final class DefaultRouterApiFactory implements RouterApiFactory {

    @Override
    public RouterApi create(String baseUrl) {
        return new HttpRouterApi(baseUrl);
    }
}
