package com.viafoura.template.microservice.vertx.api.response;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.*;

class ApiResponseTest {

    @Test
    void givenApiResponse_whenConstructor_thenSuccess() {
        ApiResponse<String> response = new ApiResponse<>(200, "OK", "Success");
        assertionsWithOKResponse(response);
    }

    @Test
    void givenApiResponse_whenConstructorWithHttpResponseStatus_thenSuccess() {
        ApiResponse<String> response = new ApiResponse<>(HttpResponseStatus.OK, "Success");
        assertionsWithOKResponse(response);
    }

    void assertionsWithOKResponse(ApiResponse<String> response) {
        assertNotNull(response);
        assertNotNull(response.getHeaders());
        assertEquals(1, response.getHeaders().size());
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getStatusMessage());
        assertEquals("Success", response.getPayload());
    }
}
