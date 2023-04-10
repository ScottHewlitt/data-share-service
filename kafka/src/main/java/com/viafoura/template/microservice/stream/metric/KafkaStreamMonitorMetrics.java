package com.viafoura.template.microservice.stream.metric;

public interface KafkaStreamMonitorMetrics {
    void kafkaStreamFailure(Object... args);
    void kafkaStreamCreate(Object... args);
    void kafkaStreamHeartbeat(Object... args);
}
