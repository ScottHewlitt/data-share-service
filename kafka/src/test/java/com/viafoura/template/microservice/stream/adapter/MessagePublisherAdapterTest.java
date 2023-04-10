package com.viafoura.template.microservice.stream.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafoura.template.microservice.business.domain.event.StreamEvent;
import com.viafoura.template.microservice.stream.runner.producer.EventPublisher;
import java.util.HashMap;
import org.junit.jupiter.api.*;

class MessagePublisherAdapterTest {

    private final EventPublisher eventPublisher = mock(EventPublisher.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final String jsonValue = "{\"hello\":\"world\"}";
    private MessagePublisherAdapter messagePublisherAdapter;

    @BeforeEach
    void setup() {
        messagePublisherAdapter = new MessagePublisherAdapter(eventPublisher, objectMapper);
    }

    @Test
    void givenStreamEventAndTopic_whenPublish_thenSuccess() throws JsonProcessingException {
        StreamEvent event = buildEvent();
        String topic = "topic";
        when(objectMapper.writeValueAsString(event)).thenReturn(jsonValue);
        assertDoesNotThrow(() -> messagePublisherAdapter.publishMessageToOutgoingTopic(event, topic));
        verify(objectMapper, times(1)).writeValueAsString(event);
        verify(eventPublisher, times(1)).publish(topic, jsonValue);
    }

    @Test
    void givenStreamEvent_whenPublishToAllTopics_thenSuccess() throws JsonProcessingException {
        StreamEvent event = buildEvent();
        when(objectMapper.writeValueAsString(event)).thenReturn(jsonValue);
        assertDoesNotThrow(() -> messagePublisherAdapter.publishMessageToAllTopics(event));
        verify(objectMapper, times(1)).writeValueAsString(event);
        verify(eventPublisher, times(1)).publishToAllTopics(jsonValue);
    }

    @Test
    void givenNullEvent_whenPublishToAllTopics_thenSuccess() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        assertThrows(RuntimeException.class,
                () -> messagePublisherAdapter.publishMessageToAllTopics(null));
    }

    StreamEvent buildEvent() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("key", "value");
        return new StreamEvent(hashMap);
    }
}