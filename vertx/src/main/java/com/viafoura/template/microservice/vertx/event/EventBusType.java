package com.viafoura.template.microservice.vertx.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventBusType {

    HEALTHY("healthy", "service.healthy");

    private final String operationId;
    private final String address;

}
