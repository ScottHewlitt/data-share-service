package com.viafoura.template.microservice.vertx.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class OperationModelKeyTypeTest {

    @Test
    void givenOperationModelKeyType_whenConstruct_thenSuccess() {
        OperationModelKeyType operationModelKeyType = OperationModelKeyType.OPERATION_POJO;
        assertEquals("operationPOJO", operationModelKeyType.getKey());
    }

}