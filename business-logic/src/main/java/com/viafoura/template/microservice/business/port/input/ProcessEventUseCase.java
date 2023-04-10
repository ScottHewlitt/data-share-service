package com.viafoura.template.microservice.business.port.input;

import com.viafoura.template.microservice.business.domain.event.StreamEvent;

public interface ProcessEventUseCase {

    void processEvent(StreamEvent event);

}
