package com.viafoura.template.microservice.di.module;

import static org.junit.jupiter.api.Assertions.*;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;

class MetricsModuleTest extends ModuleTester {

    @Test
    void givenMetricsModule_whenCreateInjector_thenInjectMeterRegistryClasses() {
        assertNotNull(injector.getInstance(MeterRegistry.class));
    }

}