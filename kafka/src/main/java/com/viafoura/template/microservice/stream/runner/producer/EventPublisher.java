package com.viafoura.template.microservice.stream.runner.producer;

import com.viafoura.template.microservice.stream.config.StreamConfig;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor = @__({@Inject}))
public class EventPublisher implements AutoCloseable {

    private final Producer<String, String> producer;
    private final StreamConfig streamConfig;

    public void publish(String topic, String json) {
        producer.send(new ProducerRecord<>(topic, json), this::producerCallback);
    }

    public void publishToAllTopics(String json) {
        streamConfig.getOutgoingTopics().forEach(
                topic -> producer.send(new ProducerRecord<>(topic, json), this::producerCallback));
    }

    @Override
    public void close() {
        producer.close();
    }

    private void producerCallback(RecordMetadata recordMetadata, Exception error) {
        if (error == null) {
            log.debug("Successfully sent message to topic {}", recordMetadata.topic());
        } else {
            log.error("Failed to send message to kafka.  Metadata = {}", recordMetadata, error);
        }
    }
}
