package com.viafoura.template.microservice.vertx.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class EventBusTypeTest {

    @Test
    void givenEventBusType_whenConstruct_thenSuccess() {
        EventBusType eventBusType = EventBusType.HEALTHY;
        assertEquals("healthy", eventBusType.getOperationId());
        assertEquals("service.healthy", eventBusType.getAddress());
    }
}