package com.viafoura.template.microservice.vertx.di;

import static org.junit.jupiter.api.Assertions.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;

class InjectorProviderTest {

    @Test
    void givenInjectorProvider_whenInitialize_thenSuccess() {
        Injector injector = Guice.createInjector();
        InjectorProvider.initialize(injector);
        assertNotNull(InjectorProvider.getInstance());
        assertEquals(InjectorProvider.getInstance().getInjector(), injector);
    }
}