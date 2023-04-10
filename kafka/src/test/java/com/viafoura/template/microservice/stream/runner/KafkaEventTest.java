package com.viafoura.template.microservice.stream.runner;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;

class KafkaEventTest {

    @Test
    void givenNewStreamEvent_whenCreateWithNoArgs_thenSuccess() {
        assertDoesNotThrow(() -> new KafkaEvent(new HashMap<>()));
    }

    @Test
    void givenNewStreamEvent_whenCreateWithArgs_thenSuccess() {
        Map<String, Object> map = Map.of("hello","viafoura");
        KafkaEvent kafkaEvent = new KafkaEvent(map);
        assertEquals(1, kafkaEvent.getActualEvent().size());
    }
}
