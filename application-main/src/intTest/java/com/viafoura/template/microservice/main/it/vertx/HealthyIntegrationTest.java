package com.viafoura.template.microservice.main.it.vertx;

import com.viafoura.template.microservice.di.util.GuiceInjectorFactory;
import com.viafoura.template.microservice.vertx.di.InjectorProvider;
import com.viafoura.template.microservice.vertx.verticle.MainVerticle;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
public class HealthyIntegrationTest {

    private static final String LOCALHOST = "localhost";
    private static final int PORT = 8080;
    private static final String REQUEST_URL = "/healthy";
    private Vertx vertx;

    @BeforeEach
    void setUp() {
        InjectorProvider.initialize(GuiceInjectorFactory.getOrCreateInjector());
        vertx = Vertx.vertx();
    }

    @AfterEach
    public void tearDown() {
        vertx.close();
    }

    @Test
    void givenServer_whenRequestHealthy_thenReturnNoContent(VertxTestContext testContext) {
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id ->
                WebClient.create(vertx).get(PORT, LOCALHOST, REQUEST_URL)
                .as(BodyCodec.string())
                .send(testContext.succeeding(resp -> testContext.verify(() -> {
                    Assertions.assertEquals(HttpResponseStatus.NO_CONTENT.code(), resp.statusCode());
                    testContext.completeNow();
                })))));
    }
}
