package com.viafoura.template.microservice.metrics.adapter;

import com.viafoura.template.microservice.business.infrastructure.metric.MetricNames;
import com.viafoura.template.microservice.business.port.output.metric.ApplicationMetricsPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class ServiceMetricsAdapter implements ApplicationMetricsPort {

    private final MeterRegistry meterRegistry;
    private final Counter countHealthyCalls;
    private final Counter countVerifiedEvents;
    @Inject
    public ServiceMetricsAdapter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        countHealthyCalls = buildCounter(MetricNames.HEALTH_CHECK_COUNT, "Number of healthy endpoint calls");
        countVerifiedEvents = buildCounter(MetricNames.VERIFIED_EVENT_COUNT, "Number of verified event calls");
    }

    @Override
    public void incrementHealthyCalls() {
        incrementAndLog(countHealthyCalls);
    }

    @Override
    public void incrementVerifiedEvents() {
        incrementAndLog(countVerifiedEvents);
    }

    private Counter buildCounter(String name, String description) {
        return Counter.builder(name).description(description).register(meterRegistry);
    }

    private void incrementAndLog(Counter metric) {
        metric.increment();
        log.debug("{}: {}", metric.getId().getDescription(), metric.count());
    }
}
