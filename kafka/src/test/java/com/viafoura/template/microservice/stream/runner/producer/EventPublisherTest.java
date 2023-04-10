package com.viafoura.template.microservice.stream.runner.producer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.viafoura.template.microservice.stream.config.StreamConfig;
import java.util.List;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.*;

class EventPublisherTest {

    private final Producer<String, String> producer = mock(Producer.class);
    private final StreamConfig streamConfig = mock(StreamConfig.class);
    private EventPublisher eventPublisher;

    @BeforeEach
    void setup() {
        eventPublisher = new EventPublisher(producer, streamConfig);
    }

    @Test
    void givenPublisher_whenPublishToOneTopic_thenSuccess() {
        eventPublisher.publish("topic", "{1:2}");
        verify(producer, times(1)).send(any(ProducerRecord.class), any());
    }

    @Test
    void givenPublisher_whenPublishToAllTopics_thenSuccess() {
        when(streamConfig.getOutgoingTopics()).thenReturn(List.of("topic_1","topic_2"));
        eventPublisher.publishToAllTopics("{1:2}");
        verify(producer, times(2)).send(any(ProducerRecord.class), any());
    }

    @Test
    void givenPublisher_whenClose_thenSuccess() {
        eventPublisher.close();
        verify(producer, times(1)).close();
    }
}
