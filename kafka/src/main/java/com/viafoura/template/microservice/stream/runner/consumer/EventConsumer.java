package com.viafoura.template.microservice.stream.runner.consumer;

import com.viafoura.template.microservice.stream.runner.KafkaEvent;

public interface EventConsumer {

    void consumeEvent(KafkaEvent event);

}
