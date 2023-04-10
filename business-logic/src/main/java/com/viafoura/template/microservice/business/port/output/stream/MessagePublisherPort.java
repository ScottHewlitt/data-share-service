package com.viafoura.template.microservice.business.port.output.stream;

import com.viafoura.template.microservice.business.domain.event.StreamEvent;

public interface MessagePublisherPort {

    void publishMessageToOutgoingTopic(StreamEvent streamEvent, String outgoingTopic);

    void publishMessageToAllTopics(StreamEvent streamEvent);

}
