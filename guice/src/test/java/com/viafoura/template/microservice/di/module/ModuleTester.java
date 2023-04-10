package com.viafoura.template.microservice.di.module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.viafoura.template.microservice.di.util.GuiceInjectorFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;

class ModuleTester {

    Injector injector;

    @BeforeEach
    void setup() {
        injector = GuiceInjectorFactory.getOrCreateInjector();
    }
}
