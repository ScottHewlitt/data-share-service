package com.viafoura.template.microservice.stream.metric;

import static com.viafoura.template.microservice.stream.metric.StreamMetricNames.KAFKA_STREAM_CREATIONS;
import static com.viafoura.template.microservice.stream.metric.StreamMetricNames.KAFKA_STREAM_FAILURES;
import static com.viafoura.template.microservice.stream.metric.StreamMetricNames.KAFKA_STREAM_HEARTBEATS;
import static org.junit.jupiter.api.Assertions.*;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

class StreamMonitorMetricsTest {

    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private StreamMonitorMetrics streamMonitorMetrics;

    @BeforeEach
    void setup() {
        streamMonitorMetrics = new StreamMonitorMetrics(meterRegistry);
    }

    @Test
    void givenMonitorMetrics_whenKafkaStreamFailure_thenIncrementLogs() {
        streamMonitorMetrics.kafkaStreamFailure();
        Counter counter = getCounterByName(KAFKA_STREAM_FAILURES);
        assertEquals(1, counter.count());
    }

    @Test
    void givenMonitorMetrics_whenKafkaStreamCreate_thenIncrementLogs() {
        streamMonitorMetrics.kafkaStreamCreate();
        Counter counter = getCounterByName(KAFKA_STREAM_CREATIONS);
        assertEquals(1, counter.count());
    }

    @Test
    void givenMonitorMetrics_whenKafkaStreamHeartbeat_thenIncrementLogs() {
        streamMonitorMetrics.kafkaStreamHeartbeat();
        Counter counter = getCounterByName(KAFKA_STREAM_HEARTBEATS);
        assertEquals(1, counter.count());
    }

    private Counter getCounterByName(String name) {
        return (Counter) meterRegistry.getMeters().stream()
                .filter(m -> m.getId().getName().equals(name))
                .findFirst().get();
    }
}
