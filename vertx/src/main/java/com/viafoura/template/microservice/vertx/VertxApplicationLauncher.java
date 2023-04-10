package com.viafoura.template.microservice.vertx;

import com.google.inject.Injector;
import com.viafoura.template.microservice.metrics.MicrometerRegistryFactory;
import com.viafoura.template.microservice.vertx.di.InjectorProvider;
import com.viafoura.template.microservice.vertx.verticle.MainVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxApplicationLauncher extends Launcher {

    @Override
    public void beforeStartingVertx(VertxOptions options) {
        // Enable Vert.x metrics
        options.setMetricsOptions(
                new MicrometerMetricsOptions()
                        .setMicrometerRegistry(MicrometerRegistryFactory.create())
                        .setEnabled(true)
        );
    }

    public void run(Injector injector) {
        InjectorProvider.initialize(injector);
        new VertxApplicationLauncher().dispatch(new String[]{"run", MainVerticle.class.getName()});
    }
}
