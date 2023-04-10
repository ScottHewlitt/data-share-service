package com.viafoura.template.microservice.di.module;

import static java.util.Objects.requireNonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import com.viafoura.template.microservice.vertx.config.VertxConfig;
import com.viafoura.template.microservice.vertx.config.VertxConfigImpl;
import com.viafoura.template.microservice.vertx.event.EventBusApiConsumer;
import com.viafoura.template.microservice.vertx.event.consumer.HealthCheckEventBusApiConsumer;
import com.viafoura.template.microservice.vertx.security.OperationModelKeyType;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import javax.inject.Singleton;

public class VertxModule extends AbstractModule {

    private final Vertx vertx;
    private final Config config;

    public VertxModule(Config config) {
        this.config = requireNonNull(config);
        this.vertx = Vertx.vertx();
    }

    @Override
    protected void configure() {
        VertxConfigImpl vertxConfigImpl = new VertxConfigImpl(config);
        bind(VertxConfig.class).toInstance(vertxConfigImpl);
        bind(Vertx.class).toInstance(vertx);
        bindEventBusApiConsumer();
    }

    @Provides
    @Singleton
    HttpServerOptions provideHttpServerOptions(VertxConfig vertxConfig) {
        return new HttpServerOptions()
                .setPort(vertxConfig.getServerPort())
                .setCompressionSupported(true)
                .setDecompressionSupported(true);
    }

    @Provides
    @Singleton
    RouterBuilderOptions provideRouterFactoryOptions() {
        return new RouterBuilderOptions().setOperationModelKey(OperationModelKeyType.OPERATION_POJO.getKey());
    }

    private void bindEventBusApiConsumer() {
        Multibinder<EventBusApiConsumer> multiBinder = Multibinder.newSetBinder(binder(), EventBusApiConsumer.class);
        multiBinder.addBinding().to(HealthCheckEventBusApiConsumer.class);
    }
}
