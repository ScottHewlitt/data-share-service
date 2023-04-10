package com.viafoura.template.microservice.vertx.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.viafoura.template.microservice.business.port.input.HealthCheckUseCase;
import com.viafoura.template.microservice.vertx.api.HealthCheckApi;
import com.viafoura.template.microservice.vertx.api.exception.ApiException;
import com.viafoura.template.microservice.vertx.api.response.ApiResponse;
import org.junit.jupiter.api.*;

class HealthCheckApiTest {

    private final HealthCheckUseCase healthCheckUseCase = mock(HealthCheckUseCase.class);
    private static final int API_RESPONSE_NO_CONTENT = 204;
    private HealthCheckApi healthCheckApi;

    @BeforeEach
    void setup() {
        healthCheckApi = new HealthCheckApiAdapter(healthCheckUseCase);
    }

    @Test
    void givenHealthyUseCase_whenHealthyCheck_thenSuccess() throws ApiException {
        when(healthCheckUseCase.isHealthy()).thenReturn(Boolean.TRUE);
        ApiResponse<Void> response = healthCheckApi.getHealthyResponse();
        assertEquals(API_RESPONSE_NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenHealthyUseCase_whenHealthyCheck_thenServiceUnavailable() {
        when(healthCheckUseCase.isHealthy()).thenReturn(Boolean.FALSE);
        assertThrows(ApiException.class, () ->healthCheckApi.getHealthyResponse());
    }
}
