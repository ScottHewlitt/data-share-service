package com.viafoura.template.microservice.vertx.verticle;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Injector;
import com.viafoura.template.microservice.metrics.MicrometerRegistryFactory;
import com.viafoura.template.microservice.stream.runner.rx.KafkaStreamSourceMonitor;
import com.viafoura.template.microservice.vertx.di.InjectorProvider;
import com.viafoura.template.microservice.vertx.error.RoutingErrorHandler;
import com.viafoura.template.microservice.vertx.event.EventBusApiConsumer;
import com.viafoura.template.microservice.vertx.event.EventBusApiConsumerRegistry;
import com.viafoura.template.microservice.vertx.event.EventBusType;
import com.viafoura.template.microservice.vertx.handler.CorsHandlerFactory;
import com.viafoura.template.microservice.vertx.security.SecuritySchemaType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {
    private static final String API_SPEC_FILE = "api-spec.yaml";
    private static final String METRICS_OPERATION_ID = "metrics";

    private HttpServer httpServer;
    private EventBusApiConsumerRegistry eventBusApiConsumerRegistry;
    private KafkaStreamSourceMonitor streamSourceMonitor;

    @Override
    public void start(Promise<Void> promise) {
        getVertx().executeBlocking(this::initialize, result -> {
            if (result.failed()) {
                log.error("Verticle deployment failed!", result.cause());
                promise.fail(result.cause());
            } else {
                log.info("Verticle deployed successfully.");
                promise.complete();
            }
        });
    }

    private void initialize(Promise<Void> promise) {
        Injector injector = getInjector();
        eventBusApiConsumerRegistry = injector.getInstance(EventBusApiConsumerRegistry.class);
        streamSourceMonitor = injector.getInstance(KafkaStreamSourceMonitor.class);
        HttpServerOptions httpServerOptions = injector.getInstance(HttpServerOptions.class);
        RouterBuilderOptions routerBuilderOptions = injector.getInstance(RouterBuilderOptions.class);

        configureJackson();

        RouterBuilder.create(getVertx(), API_SPEC_FILE, result -> {
            if (result.failed()) {
                promise.fail(result.cause());
                return;
            }

            RouterBuilder builder = result.result();
            builder.setOptions(routerBuilderOptions);
            addSecurityHandlers(builder);
            addGlobalHandlers(builder);
            addPrometheusMetricsHandler(builder);

            eventBusApiConsumerRegistry.getApiConsumers().forEach(EventBusApiConsumer::start);
            mountEventBusAndAddErrorHandlers(builder);

            httpServer = getVertx().createHttpServer(httpServerOptions);

            httpServer.requestHandler(builder.createRouter()).listen(serverStartupResult -> {
                if (serverStartupResult.failed()) {
                    promise.fail(serverStartupResult.cause());
                } else {
                    promise.complete();
                }
            });

            streamSourceMonitor.start();
        });
    }

    private static void configureJackson() {
        DatabindCodec.mapper().registerModule(new JavaTimeModule());
        DatabindCodec.prettyMapper().registerModule(new JavaTimeModule());
    }

    private static void addGlobalHandlers(RouterBuilder builder) {
        builder.rootHandler(CorsHandlerFactory.createInstance());
        builder.rootHandler(BodyHandler.create());
    }

    private void addSecurityHandlers(RouterBuilder builder) {
        builder.securityHandler(SecuritySchemaType.TOKEN_IN_COOKIE.getName(), RoutingContext::next);
        builder.securityHandler(SecuritySchemaType.TOKEN.getName(), RoutingContext::next);
    }

    private void addPrometheusMetricsHandler(RouterBuilder builder) {
        builder.operation(METRICS_OPERATION_ID).handler(
                context -> context.response().end(MicrometerRegistryFactory.prometheusMeterRegistry.scrape()));
    }

    private static void mountEventBusAndAddErrorHandlers(RouterBuilder builder) {
        final RoutingErrorHandler failureHandler = new RoutingErrorHandler();
        Arrays.stream(EventBusType.values())
                .forEach(event -> builder
                        .operation(event.getOperationId())
                        .routeToEventBus(event.getAddress())
                        .failureHandler(failureHandler));
    }

    @Override
    public void stop(Promise<Void> promise) {
        // Stopping each ServiceConnector before is an attempt to avoid weird states if the server crashes
        eventBusApiConsumerRegistry.getApiConsumers().forEach(EventBusApiConsumer::stop);
        httpServer.close();
        streamSourceMonitor.stop();
        promise.complete();
    }

    protected Injector getInjector() {
        return InjectorProvider.getInstance().getInjector();
    }

    @Override
    public Vertx getVertx() {
        return InjectorProvider.getInstance().getInjector().getInstance(Vertx.class);
    }
}
