package com.viafoura.template.microservice.di.module;

import com.google.inject.AbstractModule;
import com.viafoura.template.microservice.metrics.MicrometerRegistryFactory;
import io.micrometer.core.instrument.MeterRegistry;

public class MetricsModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        bind(MeterRegistry.class).toInstance(MicrometerRegistryFactory.get());
    }
}
