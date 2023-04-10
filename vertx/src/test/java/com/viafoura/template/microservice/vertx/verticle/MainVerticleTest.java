package com.viafoura.template.microservice.vertx.verticle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.inject.Injector;
import com.viafoura.template.microservice.stream.runner.rx.KafkaStreamSourceMonitor;
import com.viafoura.template.microservice.vertx.api.HealthCheckApi;
import com.viafoura.template.microservice.vertx.api.exception.ApiException;
import com.viafoura.template.microservice.vertx.api.response.ApiResponseFactory;
import com.viafoura.template.microservice.vertx.di.InjectorProvider;
import com.viafoura.template.microservice.vertx.event.EventBusApiConsumerRegistry;
import com.viafoura.template.microservice.vertx.event.consumer.HealthCheckEventBusApiConsumer;
import com.viafoura.template.microservice.vertx.security.OperationModelKeyType;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.openapi.RouteNamingStrategy;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
class MainVerticleTest {

    private final int PORT = 8080;
    private final Vertx vertx = Vertx.vertx();
    private final Injector injector = mock(Injector.class, RETURNS_DEEP_STUBS);
    private final KafkaStreamSourceMonitor streamSourceMonitor = mock(KafkaStreamSourceMonitor.class);
    private final HttpServerOptions httpServerOptions = new HttpServerOptions()
            .setPort(PORT)
            .setCompressionSupported(true)
            .setDecompressionSupported(true);
    private final RouterBuilderOptions routerBuilderOptions = mock(RouterBuilderOptions.class);
    private final HealthCheckApi healthCheckApi = mock(HealthCheckApi.class);

    @BeforeEach
    void setup(VertxTestContext vertxTestContext) throws ApiException {
        HealthCheckEventBusApiConsumer healthCheckEventBusApiConsumer =
                new HealthCheckEventBusApiConsumer(vertx, healthCheckApi);
        EventBusApiConsumerRegistry eventBusApiConsumerRegistry =
                new EventBusApiConsumerRegistry(Set.of(healthCheckEventBusApiConsumer));

        when(injector.getInstance(Vertx.class)).thenReturn(vertx);
        when(injector.getInstance(EventBusApiConsumerRegistry.class)).thenReturn(eventBusApiConsumerRegistry);
        when(injector.getInstance(KafkaStreamSourceMonitor.class)).thenReturn(streamSourceMonitor);
        when(injector.getInstance(HttpServerOptions.class)).thenReturn(httpServerOptions);
        when(injector.getInstance(RouterBuilderOptions.class)).thenReturn(routerBuilderOptions);
        when(routerBuilderOptions.getRouteNamingStrategy()).thenReturn(RouteNamingStrategy.OPERATION_OPENAPI_PATH);
        when(routerBuilderOptions.getOperationModelKey()).thenReturn(OperationModelKeyType.OPERATION_POJO.getKey());
        when(healthCheckApi.getHealthyResponse()).thenReturn(ApiResponseFactory.noContent());

        InjectorProvider.initialize(injector);
        MainVerticle mainVerticle = new MainVerticle();
        vertx.deployVerticle(mainVerticle)
                .onSuccess(ok -> vertxTestContext.completeNow())
                .onFailure(vertxTestContext::failNow);
    }

    @AfterEach
    void tearDown() {
        vertx.close();
    }

    @Test
    void givenMainVerticle_whenDeploy_thenSuccess(VertxTestContext vertxTestContext) {
        vertx.createHttpClient()
                .request(HttpMethod.GET, PORT, "localhost", "/healthy")
                .compose(HttpClientRequest::send)
                .compose(HttpClientResponse::body)
                .onSuccess(body -> vertxTestContext.verify(() -> {
                    assertTrue(body.toString().isEmpty());
                    vertxTestContext.completeNow();
                }))
                .onFailure(vertxTestContext::failNow);
    }
}
