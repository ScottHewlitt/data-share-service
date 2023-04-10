package com.viafoura.template.microservice.metrics;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import io.micrometer.statsd.StatsdMeterRegistry;
import java.util.Properties;
import javax.annotation.Nonnull;

@Singleton
public class MicrometerRegistryFactory {

    private static final Config config = ConfigFactory.load();

    private static final CompositeMeterRegistry applicationMetrics = new CompositeMeterRegistry();

    public static final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(
            PrometheusConfig.DEFAULT);

    private static final Properties properties = new Properties();

    static {
        config.entrySet().forEach(entry -> properties.put(entry.getKey(), entry.getValue().unwrapped().toString()));
    }

    private MicrometerRegistryFactory() {
    }

    public static CompositeMeterRegistry create() {
        MeterRegistry genericMeterRegistry = setupGenericMeterRegistry(buildStatsdMeterRegistry());
        applicationMetrics.add(genericMeterRegistry);

        // Add prefix filter
        String prefix = config.getString("service.metrics.prefix");
        applicationMetrics.config().meterFilter(new MeterFilter() {
            @Override
            @Nonnull
            public Id map(@Nonnull Id id) {
                return id.withName(String.format("%s.%s", prefix, id.getName()));
            }
        });

        // Add as the primary registry
        Metrics.addRegistry(applicationMetrics);

        return applicationMetrics;
    }

    private static StatsdMeterRegistry buildStatsdMeterRegistry() {
        return new StatsdMeterRegistry(new StatsdConfig() {
            @Override
            public String get(String key) {
                return properties.getProperty(key);
            }

            @Override
            public String prefix() {
                return "service.metrics.datadog";
            }

            @Override
            public StatsdFlavor flavor() {
                return StatsdFlavor.DATADOG;
            }
        }, Clock.SYSTEM);
    }

    private static MeterRegistry setupGenericMeterRegistry(StatsdMeterRegistry statsdMeterRegistry) {

        CompositeMeterRegistry genericMeterRegistry = new CompositeMeterRegistry();
        JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(new JmxConfig() {
            @Override
            public String get(@Nonnull String key) {
                return properties.getProperty(key);
            }

            @Override
            @Nonnull
            public String prefix() {
                return "service.metrics." + JmxConfig.super.prefix();
            }
        }, Clock.SYSTEM);

        genericMeterRegistry.add(jmxMeterRegistry);
        genericMeterRegistry.add(statsdMeterRegistry);
        genericMeterRegistry.add(prometheusMeterRegistry);

        new ClassLoaderMetrics().bindTo(genericMeterRegistry);
        new JvmMemoryMetrics().bindTo(genericMeterRegistry);
        try(JvmGcMetrics jvmGcMetrics = new JvmGcMetrics()) {
            jvmGcMetrics.bindTo(genericMeterRegistry);
        }
        new ProcessorMetrics().bindTo(genericMeterRegistry);
        new JvmThreadMetrics().bindTo(genericMeterRegistry);
        new ProcessMemoryMetrics().bindTo(genericMeterRegistry);
        new ProcessThreadMetrics().bindTo(genericMeterRegistry);

        return genericMeterRegistry;
    }

    public static MeterRegistry get() {
        return applicationMetrics;
    }
}
