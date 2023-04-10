package com.viafoura.template.microservice.stream.runner;

import com.viafoura.template.microservice.stream.config.StreamConfig;
import com.viafoura.template.microservice.stream.runner.consumer.TopologySupplier;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.apache.kafka.streams.KafkaStreams;


public class KafkaStreamsSource implements KafkaStreamSource {

    private final Supplier<KafkaStreams> kafkaStreamsSupplier;

    private KafkaStreams kafkaStreams;

    @Inject
    public KafkaStreamsSource(TopologySupplier topologySupplier, StreamConfig config) {
        this(() -> new KafkaStreams(topologySupplier.get(), config.toProperties()));
    }

    /* for testing */
    KafkaStreamsSource(Supplier<KafkaStreams> kafkaStreamsSupplier) {
        this.kafkaStreamsSupplier = kafkaStreamsSupplier;
    }

    @Override
    public void start() {
        if (kafkaStreams == null) {
            kafkaStreams = kafkaStreamsSupplier.get();
        }
        kafkaStreams.start();
    }

    @Override
    public KafkaStreams.State state() {
        if (kafkaStreams == null) {
            throw new NoSuchElementException("Checking state of nonexistent kafka stream");
        }
        return kafkaStreams.state();
    }

    @Override
    public void close() {
        if (kafkaStreams != null) {
            kafkaStreams.close();
        }
    }

    @Override
    public void cleanUp() {
        if (kafkaStreams != null) {
            kafkaStreams.cleanUp();
        }
        kafkaStreams = null;
    }
}
