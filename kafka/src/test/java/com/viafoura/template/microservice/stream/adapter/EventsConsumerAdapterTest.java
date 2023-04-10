package com.viafoura.template.microservice.stream.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.viafoura.template.microservice.business.domain.event.StreamEvent;
import com.viafoura.template.microservice.business.port.input.ProcessEventUseCase;
import com.viafoura.template.microservice.stream.runner.KafkaEvent;
import java.util.HashMap;
import org.junit.jupiter.api.*;

class EventsConsumerAdapterTest {
    private final ProcessEventUseCase processEventUseCase = mock(ProcessEventUseCase.class);
    private final KafkaEvent kafkaEvent = new KafkaEvent(new HashMap<>());
    private EventsConsumerAdapter eventsConsumerAdapter;

    @BeforeEach
    void setup() {
        eventsConsumerAdapter = new EventsConsumerAdapter(processEventUseCase);
    }

    @Test
    void givenEvent_whenConsumeEvent_thenSuccess() {
        assertDoesNotThrow(() -> eventsConsumerAdapter.consumeEvent(kafkaEvent));
        verify(processEventUseCase, times(1)).processEvent(any(StreamEvent.class));
    }
}