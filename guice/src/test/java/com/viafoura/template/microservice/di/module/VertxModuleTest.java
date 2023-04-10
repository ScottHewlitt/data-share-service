package com.viafoura.template.microservice.di.module;

import static org.junit.jupiter.api.Assertions.*;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.viafoura.template.microservice.vertx.config.VertxConfig;
import com.viafoura.template.microservice.vertx.event.EventBusApiConsumer;
import com.viafoura.template.microservice.vertx.event.consumer.HealthCheckEventBusApiConsumer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;

class VertxModuleTest extends ModuleTester {

    @Test
    void givenVertxModule_whenCreateInjector_thenInject() {
        assertNotNull(injector.getInstance(Vertx.class));
        assertNotNull(injector.getInstance(VertxConfig.class));
        assertNotNull(injector.getInstance(HttpServerOptions.class));
        assertNotNull(injector.getInstance(RouterBuilderOptions.class));

        TypeLiteral<Set<EventBusApiConsumer>> eventBusApiConsumerSet = new TypeLiteral<>() {};
        Key<Set<EventBusApiConsumer>> eventBusApiConsumerKeys = Key.get(eventBusApiConsumerSet);
        Set<EventBusApiConsumer> eventBusApiImplementations = injector.getInstance(eventBusApiConsumerKeys);
        assertEquals(eventBusApiImplementations.stream().map(EventBusApiConsumer::getClass).collect(Collectors.toSet()),
                Set.of(HealthCheckEventBusApiConsumer.class));
    }
}
