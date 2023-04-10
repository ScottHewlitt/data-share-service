package com.viafoura.template.microservice.di.module;

import static org.junit.jupiter.api.Assertions.*;

import com.viafoura.template.microservice.business.port.output.metric.ApplicationMetricsPort;
import com.viafoura.template.microservice.business.port.output.stream.MessagePublisherPort;
import org.junit.jupiter.api.*;

class AdapterModuleTest extends ModuleTester {

    @Test
    void givenAdapterModule_whenCreateInjector_thenInjectAdapters() {
        assertNotNull(injector.getInstance(ApplicationMetricsPort.class));
        assertNotNull(injector.getInstance(MessagePublisherPort.class));
    }

}
