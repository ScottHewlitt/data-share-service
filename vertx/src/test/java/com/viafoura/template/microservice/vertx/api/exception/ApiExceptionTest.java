package com.viafoura.template.microservice.vertx.api.exception;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.*;

class ApiExceptionTest {

    @Test
    void givenApiException_whenConstructor_thenSuccess() {
        ApiException exception = new ApiException(500, "Internal Server Error", "ISE");
        assertionsWithInternalServerError(exception);
    }

    @Test
    void givenApiException_whenConstructorWithHttpResponseStatus_thenSuccess() {
        ApiException exception = new ApiException(HttpResponseStatus.INTERNAL_SERVER_ERROR, "ISE");
        assertionsWithInternalServerError(exception);
    }

    void assertionsWithInternalServerError(ApiException exception) {
        assertNotNull(exception);
        assertNotNull(exception.getHeaders());
        assertEquals(1, exception.getHeaders().size());
        assertEquals(500, exception.getStatusCode());
        assertEquals("Internal Server Error", exception.getStatusMessage());
        assertEquals("ISE", exception.getPayload());
    }
}
