package com.viafoura.template.microservice.vertx.api.response;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.*;

class ApiResponseFactoryTest {

    @Test
    void givenApiResponseFactory_whenCreated_thenSuccess() {
        assertEquals(ApiResponseFactory.created("Success"),
                new ApiResponse<>(HttpResponseStatus.CREATED, "Success"));
    }

    @Test
    void givenApiResponseFactory_whenOK_thenSuccess() {
        assertEquals(ApiResponseFactory.ok("Success"),
                new ApiResponse<>(HttpResponseStatus.OK, "Success"));
    }

    @Test
    void givenApiResponseFactory_whenAccepted_thenSuccess() {
        assertEquals(ApiResponseFactory.accepted("Success"),
                new ApiResponse<>(HttpResponseStatus.ACCEPTED, "Success"));
    }

    @Test
    void givenApiResponseFactory_whenNoContent_thenSuccess() {
        assertEquals(ApiResponseFactory.noContent(),
                new ApiResponse<>(HttpResponseStatus.NO_CONTENT, null));
    }

    @Test
    void givenApiResponseFactory_whenEncodedResponse_thenSuccess() {
        ApiResponse<String> input = new ApiResponse<>(HttpResponseStatus.OK, "ENCODED");
        ApiResponse<String> encoded = ApiResponseFactory.encodeResponse(input);
        assertNotNull(encoded);
        assertNotEquals(encoded.getPayload(), input.getPayload());
    }
}
