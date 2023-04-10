package com.viafoura.template.microservice.business.service;

import com.viafoura.template.microservice.business.port.input.HealthCheckUseCase;
import com.viafoura.template.microservice.business.port.output.metric.ApplicationMetricsPort;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class HealthCheckService implements HealthCheckUseCase {

    private final ApplicationMetricsPort applicationMetricsPort;
    @Override
    public boolean isHealthy() {
        applicationMetricsPort.incrementHealthyCalls();
        return Boolean.TRUE;
    }
}
