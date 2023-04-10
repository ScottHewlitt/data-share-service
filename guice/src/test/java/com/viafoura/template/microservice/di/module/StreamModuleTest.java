package com.viafoura.template.microservice.di.module;

import static org.junit.jupiter.api.Assertions.*;

import com.viafoura.template.microservice.stream.runner.consumer.EventConsumer;
import com.viafoura.template.microservice.stream.metric.KafkaStreamMonitorMetrics;
import com.viafoura.template.microservice.stream.runner.KafkaStreamSource;
import com.viafoura.template.microservice.stream.runner.consumer.TopologySupplier;
import com.viafoura.template.microservice.stream.config.StreamConfig;
import org.junit.jupiter.api.*;

class StreamModuleTest extends ModuleTester {

    @Test
    void givenStreamModule_whenCreateInjector_thenInject() {
        assertNotNull(injector.getInstance(StreamConfig.class));
        assertNotNull(injector.getInstance(KafkaStreamSource.class));
        assertNotNull(injector.getInstance(TopologySupplier.class));
        assertNotNull(injector.getInstance(EventConsumer.class));
        assertNotNull(injector.getInstance(KafkaStreamMonitorMetrics.class));
    }

}