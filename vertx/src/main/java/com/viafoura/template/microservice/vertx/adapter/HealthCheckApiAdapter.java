package com.viafoura.template.microservice.vertx.adapter;

import com.viafoura.template.microservice.business.port.input.HealthCheckUseCase;
import com.viafoura.template.microservice.vertx.api.HealthCheckApi;
import com.viafoura.template.microservice.vertx.api.exception.ApiException;
import com.viafoura.template.microservice.vertx.api.response.ApiResponse;
import com.viafoura.template.microservice.vertx.api.response.ApiResponseFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class HealthCheckApiAdapter implements HealthCheckApi {

    private final HealthCheckUseCase healthCheckUseCase;

    @Override
    public ApiResponse<Void> getHealthyResponse() throws ApiException {
        if(healthCheckUseCase.isHealthy()) {
            return ApiResponseFactory.noContent();
        } else {
            throw new ApiException(HttpResponseStatus.SERVICE_UNAVAILABLE, null);
        }
    }
}
