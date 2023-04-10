package com.viafoura.template.microservice.vertx.api;

import com.viafoura.template.microservice.vertx.api.exception.ApiException;
import com.viafoura.template.microservice.vertx.api.response.ApiResponse;

public interface HealthCheckApi {

    ApiResponse<Void> getHealthyResponse() throws ApiException;

}
