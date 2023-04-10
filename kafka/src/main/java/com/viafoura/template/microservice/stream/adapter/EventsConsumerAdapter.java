package com.viafoura.template.microservice.stream.adapter;

import com.viafoura.template.microservice.business.domain.event.StreamEvent;
import com.viafoura.template.microservice.business.port.input.ProcessEventUseCase;
import com.viafoura.template.microservice.stream.runner.KafkaEvent;
import com.viafoura.template.microservice.stream.runner.consumer.EventConsumer;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class EventsConsumerAdapter implements EventConsumer {

    private final ProcessEventUseCase incomingEventUseCase;

    @Override
    public void consumeEvent(KafkaEvent event) {
        incomingEventUseCase.processEvent(new StreamEvent(event.getActualEvent()));
    }
}
