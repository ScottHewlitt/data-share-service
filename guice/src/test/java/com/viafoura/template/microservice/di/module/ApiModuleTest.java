package com.viafoura.template.microservice.di.module;

import static org.junit.jupiter.api.Assertions.*;

import com.viafoura.template.microservice.vertx.api.HealthCheckApi;
import org.junit.jupiter.api.*;

class ApiModuleTest extends ModuleTester {

    @Test
    void givenApiModule_whenCreateInjector_thenInjectApiImplementations() {
        assertNotNull(injector.getInstance(HealthCheckApi.class));
    }
}
