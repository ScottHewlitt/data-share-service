package com.viafoura.template.microservice.vertx.di;

import com.google.inject.Injector;
import com.google.inject.Provides;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public final class InjectorProvider {

    private final Injector injector;
    private static InjectorProvider instance;

    private InjectorProvider(Injector injector) {
        this.injector = injector;
        log.debug("Injector Module Provider created");
    }

    public static void initialize(Injector injector) {
        instance = new InjectorProvider(injector);
    }

    public static InjectorProvider getInstance() {
        return instance;
    }

    @Provides
    public Injector getInjector() {
        return this.injector;
    }

}
