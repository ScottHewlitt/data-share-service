package com.viafoura.template.microservice.di.module;

import com.google.inject.AbstractModule;
import com.viafoura.template.microservice.vertx.adapter.HealthCheckApiAdapter;
import com.viafoura.template.microservice.vertx.api.HealthCheckApi;

public class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        bind(HealthCheckApi.class).to(HealthCheckApiAdapter.class);
    }
}
