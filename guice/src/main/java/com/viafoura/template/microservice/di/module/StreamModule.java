package com.viafoura.template.microservice.di.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.viafoura.template.microservice.stream.runner.consumer.EventConsumer;
import com.viafoura.template.microservice.stream.runner.consumer.EventTopologySupplier;
import com.viafoura.template.microservice.stream.metric.KafkaStreamMonitorMetrics;
import com.viafoura.template.microservice.stream.runner.KafkaStreamSource;
import com.viafoura.template.microservice.stream.runner.KafkaStreamsSource;
import com.viafoura.template.microservice.stream.runner.consumer.TopologySupplier;
import com.viafoura.template.microservice.stream.adapter.EventsConsumerAdapter;
import com.viafoura.template.microservice.stream.config.StreamConfig;
import com.viafoura.template.microservice.stream.config.StreamConfigImpl;
import com.viafoura.template.microservice.stream.metric.StreamMonitorMetrics;
import java.util.Properties;
import javax.inject.Singleton;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public class StreamModule extends AbstractModule {

    private final Config config;

    public StreamModule(Config config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        super.configure();
        StreamConfigImpl streamConfig = new StreamConfigImpl(config);
        bind(StreamConfig.class).toInstance(streamConfig);
        bind(KafkaStreamSource.class).to(KafkaStreamsSource.class);
        bind(TopologySupplier.class).to(EventTopologySupplier.class);
        bind(EventConsumer.class).to(EventsConsumerAdapter.class);
        bind(KafkaStreamMonitorMetrics.class).to(StreamMonitorMetrics.class);
    }

    @Provides
    @Singleton
    Producer<String, String> provideKafkaProducer(StreamConfig config) {
        Properties properties = getKafkaProducerConfig(config);
        return new KafkaProducer<>(properties);
    }

    Properties getKafkaProducerConfig(StreamConfig config) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, config.getApplicationId());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServer());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        return properties;
    }
}
