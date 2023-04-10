package com.viafoura.template.microservice.vertx.event.consumer;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.SERVICE_UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.viafoura.template.microservice.vertx.api.HealthCheckApi;
import com.viafoura.template.microservice.vertx.api.exception.ApiException;
import com.viafoura.template.microservice.vertx.api.response.ApiResponseFactory;
import com.viafoura.template.microservice.vertx.event.EventBusType;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
class HealthCheckEventBusApiConsumerTest {

    private static final String STATUS_CODE_JSON_FIELD = "statusCode";
    private final Vertx vertx = Vertx.vertx();
    private final HealthCheckApi healthCheckApi = mock(HealthCheckApi.class);
    private HealthCheckEventBusApiConsumer healthCheckEventBusApiConsumer;

    @BeforeEach
    void setup() {
        healthCheckEventBusApiConsumer = new HealthCheckEventBusApiConsumer(vertx, healthCheckApi);
        healthCheckEventBusApiConsumer.start();
    }

    @AfterEach
    void tearDown() {
        healthCheckEventBusApiConsumer.stop();
    }

    @Test
    void givenEventConsumer_whenStart_thenConsumeEvent(VertxTestContext testContext) throws ApiException {
        when(healthCheckApi.getHealthyResponse()).thenReturn(ApiResponseFactory.noContent());
        vertx.eventBus().request(EventBusType.HEALTHY.getAddress(), null, reply -> {
            testContext.verify(() -> {
                assertNotNull(reply.result().body());
                JsonObject jsonObject = JsonObject.mapFrom(reply.result().body());
                assertEquals(NO_CONTENT.code(), jsonObject.getInteger(STATUS_CODE_JSON_FIELD));
            });
            testContext.completeNow();
        });
    }

    @Test
    void givenEventConsumerWithBadApiResponse_whenStart_thenConsumeEvent(VertxTestContext testContext)
            throws ApiException {
        when(healthCheckApi.getHealthyResponse()).thenThrow(new ApiException(SERVICE_UNAVAILABLE, null));
        vertx.eventBus().request(EventBusType.HEALTHY.getAddress(), null, reply -> {
            testContext.verify(() -> {
                assertNotNull(reply.result().body());
                JsonObject jsonObject = JsonObject.mapFrom(reply.result().body());
                assertEquals(SERVICE_UNAVAILABLE.code(), jsonObject.getInteger(STATUS_CODE_JSON_FIELD));
            });
            testContext.completeNow();
        });
    }
}