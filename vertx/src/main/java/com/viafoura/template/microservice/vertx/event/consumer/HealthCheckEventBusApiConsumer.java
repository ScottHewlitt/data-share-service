package com.viafoura.template.microservice.vertx.event.consumer;

import com.viafoura.template.microservice.vertx.api.HealthCheckApi;
import com.viafoura.template.microservice.vertx.api.exception.ApiException;
import com.viafoura.template.microservice.vertx.api.response.ApiResponse;
import com.viafoura.template.microservice.vertx.api.response.ApiResponseFactory;
import com.viafoura.template.microservice.vertx.event.EventBusApiConsumer;
import com.viafoura.template.microservice.vertx.event.EventBusType;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class HealthCheckEventBusApiConsumer implements EventBusApiConsumer {

    private final Vertx vertx;
    private final HealthCheckApi healthCheckApi;

    @Override
    public void start() {
        log.debug("Starting health check verticle");
        vertx.eventBus().<JsonObject>consumer(EventBusType.HEALTHY.getAddress())
                .handler(message -> {
                    try {
                        ApiResponse<String> encodedResponse = ApiResponseFactory
                                .encodeResponse(healthCheckApi.getHealthyResponse());
                        message.reply(new JsonObject(Json.encode(encodedResponse)));
                    } catch (ApiException e) {
                        message.reply(new JsonObject(Json.encode(e)));
                    }
                });
    }

    @Override
    public void stop() {
        log.debug("Stopping HealthCheck verticle");
    }
}
