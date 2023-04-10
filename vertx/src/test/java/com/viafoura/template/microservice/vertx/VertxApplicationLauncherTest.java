package com.viafoura.template.microservice.vertx;

import static org.junit.jupiter.api.Assertions.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.viafoura.template.microservice.vertx.di.InjectorProvider;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import org.junit.jupiter.api.*;

class VertxApplicationLauncherTest {

    @Test
    void givenVertxApplicationLauncher_whenRun_thenSuccess() {
        VertxApplicationLauncher vertxApplicationLauncher = new VertxApplicationLauncher();
        Injector injector = Guice.createInjector();
        vertxApplicationLauncher.run(injector);
        assertEquals(InjectorProvider.getInstance().getInjector(), injector);
    }

    @Test
    void givenVertxApplicationLauncher_whenBeforeStartingVertx_thenSetMetrics() {
        VertxApplicationLauncher vertxApplicationLauncher = new VertxApplicationLauncher();
        VertxOptions vertxOptions = new VertxOptions();
        vertxApplicationLauncher.beforeStartingVertx(vertxOptions);
        assertTrue(vertxOptions.getMetricsOptions() instanceof MicrometerMetricsOptions);
    }
}
