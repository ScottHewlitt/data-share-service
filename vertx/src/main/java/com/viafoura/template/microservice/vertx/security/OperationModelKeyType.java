package com.viafoura.template.microservice.vertx.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationModelKeyType {

    OPERATION_POJO("operationPOJO");
    private final String key;
}
