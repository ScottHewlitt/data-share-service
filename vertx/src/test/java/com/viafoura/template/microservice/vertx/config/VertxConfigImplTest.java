package com.viafoura.template.microservice.vertx.config;

import static org.junit.jupiter.api.Assertions.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.*;

class VertxConfigImplTest {

    @Test
    void givenConfig_whenLoadConfig_thenBindAll() {
        Config config = ConfigFactory.load();
        VertxConfigImpl vertxConfig = new VertxConfigImpl(config);

        assertTrue(vertxConfig.getServerPort() > 0);
    }
}