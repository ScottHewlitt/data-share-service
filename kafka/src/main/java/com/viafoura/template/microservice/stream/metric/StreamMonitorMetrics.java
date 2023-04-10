package com.viafoura.template.microservice.stream.metric;

import com.google.inject.Singleton;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class StreamMonitorMetrics implements KafkaStreamMonitorMetrics {

    private final Counter kafkaStreamFailures;
    private final Counter kafkaStreamCreations;
    private final Counter kafkaStreamHeartbeats;
    private final MeterRegistry meterRegistry;

    @Inject
    public StreamMonitorMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.kafkaStreamCreations = buildCounter(StreamMetricNames.KAFKA_STREAM_CREATIONS,
                "Total number of times the Kafka stream has been created");
        this.kafkaStreamHeartbeats =
                buildCounter(StreamMetricNames.KAFKA_STREAM_HEARTBEATS,
                        "Total number of heartbeats received from Kafka");
        this.kafkaStreamFailures = buildCounter(StreamMetricNames.KAFKA_STREAM_FAILURES,
                "Total number of times the Kafka stream failed to be created");
    }

    @Override
    public void kafkaStreamFailure(Object... objects) {
        incrementAndLog(kafkaStreamFailures);
    }

    @Override
    public void kafkaStreamCreate(Object... objects) {
        incrementAndLog(kafkaStreamCreations);
    }

    @Override
    public void kafkaStreamHeartbeat(Object... objects) {
        kafkaStreamHeartbeats.increment();
    }

    private Counter buildCounter(String name, String description) {
        return Counter.builder(name).description(description).register(meterRegistry);
    }

    private void incrementAndLog(Counter metric) {
        metric.increment();
        log.debug("{}: {}", metric.getId().getDescription(), metric.count());
    }
}
