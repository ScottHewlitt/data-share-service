package com.viafoura.template.microservice.stream.config;

import static org.junit.jupiter.api.Assertions.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.junit.jupiter.api.*;

class StreamConfigImplTest {

    private final Config config = ConfigFactory.load();
    @Test
    void givenConfig_whenLoadConfig_thenBindAll() {
        StreamConfigImpl streamConfig = new StreamConfigImpl(config);

        assertNotNull(streamConfig.getIncomingTopics());
        assertNotNull(streamConfig.getOutgoingTopics());
        assertNotNull(streamConfig.getApplicationId());
        assertNotNull(streamConfig.getBootstrapServer());
        assertTrue(streamConfig.getMetadataMaxAgeMs() > 0);
        assertNotNull(streamConfig.getResetPolicy());
        assertNotNull(streamConfig.getNamespace());
    }

    @Test
    void givenConfig_whenLoadConfig_thenLoadAsPropertiesAll() {
        StreamConfigImpl streamConfig = new StreamConfigImpl(config);

        Properties properties = streamConfig.toProperties();
        assertNotNull(properties.get(StreamsConfig.APPLICATION_ID_CONFIG));
        assertNotNull(properties.get(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertNotNull(properties.get(StreamsConfig.METADATA_MAX_AGE_CONFIG));
        assertNotNull(properties.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
        assertNotNull(properties.get(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG));
        assertNotNull(properties.get(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG));
        assertNotNull(properties.get(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG));
    }
}
