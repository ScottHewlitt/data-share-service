package com.viafoura.template.microservice.vertx.event;

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Getter
public class EventBusApiConsumerRegistry {

    private final Set<EventBusApiConsumer> apiConsumers;

}
