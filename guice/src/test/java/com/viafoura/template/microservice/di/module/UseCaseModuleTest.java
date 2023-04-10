package com.viafoura.template.microservice.di.module;

import static org.junit.jupiter.api.Assertions.*;

import com.viafoura.template.microservice.business.port.input.HealthCheckUseCase;
import com.viafoura.template.microservice.business.port.input.ProcessEventUseCase;
import org.junit.jupiter.api.*;

class UseCaseModuleTest extends ModuleTester {

    @Test
    void givenUseCaseModule_whenCreateInjector_thenInject() {
        assertNotNull(injector.getInstance(HealthCheckUseCase.class));
        assertNotNull(injector.getInstance(ProcessEventUseCase.class));
    }
}
