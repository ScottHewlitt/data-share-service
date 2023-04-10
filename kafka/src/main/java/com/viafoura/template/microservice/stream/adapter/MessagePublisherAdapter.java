package com.viafoura.template.microservice.stream.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafoura.template.microservice.business.domain.event.StreamEvent;
import com.viafoura.template.microservice.business.port.output.stream.MessagePublisherPort;
import com.viafoura.template.microservice.stream.runner.producer.EventPublisher;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class MessagePublisherAdapter implements MessagePublisherPort {

    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public void publishMessageToOutgoingTopic(StreamEvent streamEvent, String outgoingTopic) {
        eventPublisher.publish(outgoingTopic, getJsonFromEvent(streamEvent));
    }

    @Override
    public void publishMessageToAllTopics(StreamEvent streamEvent) {
        eventPublisher.publishToAllTopics(getJsonFromEvent(streamEvent));
    }

    private String getJsonFromEvent(StreamEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
