package com.viafoura.template.microservice.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.viafoura.template.microservice.business.domain.event.StreamEvent;
import java.util.HashMap;
import org.junit.jupiter.api.*;

class StreamEventTest {

    @Test
    void givenStreamEvent_whenAddCustomValue_thenSuccess() {
        StreamEvent event = new StreamEvent(new HashMap<>());
        event.addCustomValue("KEY", "VALUE");
        assertEquals(1, event.getValues().size());
    }

    @Test
    void givenStreamEvent_whenMarkAsVerified_thenSuccess() {
        StreamEvent event = new StreamEvent(new HashMap<>());
        event.markAsVerified();
        assertEquals(1, event.getValues().size());
        assertEquals(true, event.getValues().get("verified"));
    }
}
