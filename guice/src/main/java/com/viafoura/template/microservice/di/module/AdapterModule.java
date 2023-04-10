package com.viafoura.template.microservice.di.module;

import com.google.inject.AbstractModule;
import com.viafoura.template.microservice.business.port.output.metric.ApplicationMetricsPort;
import com.viafoura.template.microservice.business.port.output.stream.MessagePublisherPort;
import com.viafoura.template.microservice.metrics.adapter.ServiceMetricsAdapter;
import com.viafoura.template.microservice.stream.adapter.MessagePublisherAdapter;

public class AdapterModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        bind(ApplicationMetricsPort.class).to(ServiceMetricsAdapter.class);
        bind(MessagePublisherPort.class).to(MessagePublisherAdapter.class);
    }
}
