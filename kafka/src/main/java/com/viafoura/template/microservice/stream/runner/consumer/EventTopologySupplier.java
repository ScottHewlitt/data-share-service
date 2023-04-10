package com.viafoura.template.microservice.stream.runner.consumer;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafoura.template.microservice.stream.config.StreamConfig;
import com.viafoura.template.microservice.stream.runner.KafkaEvent;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

@Singleton
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Inject}))
public final class EventTopologySupplier implements TopologySupplier {
    private final StreamConfig config;
    private final EventConsumer eventConsumer;
    private final ObjectMapper objectMapper;

    @Override
    public Topology get() {
        log.info("Supplying Event topology for the input topics: {}.", String.join(", ", config.getIncomingTopics()));
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder
                .<String, String>stream(config.getIncomingTopics())
                .peek(this::peek)
                .filter((k, v) -> v != null)
                .mapValues(this::deserialize)
                .filter((k, v) -> v != null)
                .foreach(this::consume);
        return streamsBuilder.build();
    }

    private void peek(String key, String value) {
        log.trace("About to consume a message with key: '{}' and value: '{}'.", key, value);
    }

    private KafkaEvent deserialize(String value) {
        try {
            Map<String, Object> event = objectMapper.readValue(value, new TypeReference<>() {});
            return new KafkaEvent(event);
        } catch (Exception e) {
            log.warn("Unable to deserialize message {}, returning null to filter out.", value, e);
            return null;
        }
    }

    private void consume(String key, KafkaEvent event) {
        log.trace("Consuming event with key: '{}' and value: '{}'.", key, event);
        eventConsumer.consumeEvent(event);
    }
}
