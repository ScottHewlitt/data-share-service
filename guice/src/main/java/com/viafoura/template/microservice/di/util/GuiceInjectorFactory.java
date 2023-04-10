package com.viafoura.template.microservice.di.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.viafoura.template.microservice.di.module.AdapterModule;
import com.viafoura.template.microservice.di.module.ApiModule;
import com.viafoura.template.microservice.di.module.MetricsModule;
import com.viafoura.template.microservice.di.module.StreamModule;
import com.viafoura.template.microservice.di.module.UseCaseModule;
import com.viafoura.template.microservice.di.module.VertxModule;
import java.util.Objects;

@Singleton
public final class GuiceInjectorFactory {

    private static Injector injector;

    private GuiceInjectorFactory() {}

    public static Injector getOrCreateInjector() {
        if(Objects.isNull(injector)) {
            Config config = ConfigFactory.load();
            injector = Guice.createInjector(
                    new MetricsModule(),
                    new ApiModule(),
                    new VertxModule(config),
                    new StreamModule(config),
                    new AdapterModule(),
                    new UseCaseModule());
        }
        return injector;
    }


}
