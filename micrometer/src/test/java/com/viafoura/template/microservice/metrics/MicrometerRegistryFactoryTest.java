package com.viafoura.template.microservice.metrics;

import static org.junit.jupiter.api.Assertions.*;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.junit.jupiter.api.*;

class MicrometerRegistryFactoryTest {

    @Test
    void givenMicrometerRegistryFactory_whenCreate_thenGetInstance() {
        CompositeMeterRegistry compositeMeterRegistry = MicrometerRegistryFactory.create();
        assertNotNull(compositeMeterRegistry);
        assertEquals(compositeMeterRegistry, MicrometerRegistryFactory.get());
        assertNotNull(MicrometerRegistryFactory.prometheusMeterRegistry);
    }
}
