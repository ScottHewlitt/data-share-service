package com.viafoura.template.microservice.stream.runner;

import org.apache.kafka.streams.KafkaStreams;

public interface KafkaStreamSource {
    void start();
    void close();
    void cleanUp();
    KafkaStreams.State state();
}
