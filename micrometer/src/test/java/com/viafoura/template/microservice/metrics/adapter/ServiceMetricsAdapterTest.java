package com.viafoura.template.microservice.metrics.adapter;

import static com.viafoura.template.microservice.business.infrastructure.metric.MetricNames.HEALTH_CHECK_COUNT;
import static org.junit.jupiter.api.Assertions.*;

import com.viafoura.template.microservice.business.port.output.metric.ApplicationMetricsPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

class ServiceMetricsAdapterTest {
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private ApplicationMetricsPort applicationMetricsPort;

    @BeforeEach
    void setup() {
        applicationMetricsPort = new ServiceMetricsAdapter(meterRegistry);
    }

    @Test
    void givenApplicationMetrics_whenIncrementHealthyCalls_thenSuccess() {
        applicationMetricsPort.incrementHealthyCalls();
        Counter counter = meterRegistry.get(HEALTH_CHECK_COUNT).counter();
        assertEquals(1, counter.count());
    }
}