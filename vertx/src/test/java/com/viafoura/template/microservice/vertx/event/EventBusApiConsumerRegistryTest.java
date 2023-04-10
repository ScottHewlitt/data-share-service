package com.viafoura.template.microservice.vertx.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;
import org.junit.jupiter.api.*;

class EventBusApiConsumerRegistryTest {

    private final EventBusApiConsumer consumer = mock(EventBusApiConsumer.class);

    @Test
    void givenEventApiConsumerRegistry_whenConstruct_thenSuccess() {
        EventBusApiConsumerRegistry registry = new EventBusApiConsumerRegistry(Set.of(consumer));
        assertEquals(1, registry.getApiConsumers().size());
    }

}