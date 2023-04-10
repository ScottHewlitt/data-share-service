package com.viafoura.template.microservice.stream.config;

import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

public interface StreamConfig {

    List<String> getIncomingTopics();
    List<String> getOutgoingTopics();
    String getApplicationId();
    String getBootstrapServer();
    String getResetPolicy();
    int getMetadataMaxAgeMs();

    default Properties toProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, getApplicationId());
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        properties.put(StreamsConfig.METADATA_MAX_AGE_CONFIG, getMetadataMaxAgeMs());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, getResetPolicy());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        /* in production we are still on an older version of kafka, this is necessary in order for the more modern client to take in records */
        properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return properties;
    }

}
