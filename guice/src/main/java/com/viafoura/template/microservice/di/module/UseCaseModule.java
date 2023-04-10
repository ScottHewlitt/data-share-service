package com.viafoura.template.microservice.di.module;

import com.google.inject.AbstractModule;
import com.viafoura.template.microservice.business.port.input.HealthCheckUseCase;
import com.viafoura.template.microservice.business.port.input.ProcessEventUseCase;
import com.viafoura.template.microservice.business.service.HealthCheckService;
import com.viafoura.template.microservice.business.service.ProcessEventService;

public class UseCaseModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        bind(HealthCheckUseCase.class).to(HealthCheckService.class);
        bind(ProcessEventUseCase.class).to(ProcessEventService.class);
    }
}
