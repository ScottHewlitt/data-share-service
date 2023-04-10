package com.viafoura.template.microservice.stream.runner.consumer;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafoura.template.microservice.stream.config.StreamConfig;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.*;

class EventTopologySupplierTest {

    private static final String INPUT_TOPIC_NAME = "input";
    private final StreamConfig config = mock(StreamConfig.class);
    private final EventConsumer eventConsumer = mock(EventConsumer.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private EventTopologySupplier eventTopologySupplier;
    private final Serde<String> stringSerde = new Serdes.StringSerde();

    @BeforeEach
    void setup() {
        when(config.getApplicationId()).thenReturn("APP_ID");
        when(config.getBootstrapServer()).thenReturn("localhost:9092");
        when(config.getIncomingTopics()).thenReturn(List.of(INPUT_TOPIC_NAME));
        eventTopologySupplier = new EventTopologySupplier(config, eventConsumer, objectMapper);
    }

    @Test
    void givenTopologySupplier_whenGetTopology_thenSuccess() {
        eventTopologySupplier.get();
        verify(config, times(2)).getIncomingTopics();
    }

    @Test
    void givenTopologySupplier_whenEventComes_thenConsumeEvent() throws JsonProcessingException {
        Properties props = new Properties();
        props.put(APPLICATION_ID_CONFIG, config.getApplicationId());
        props.put(BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServer());

        try( Serde<String> serdeClassString = Serdes.String()){
            props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, serdeClassString.getClass().getName());
            props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, serdeClassString.getClass().getName());
        }

        try( TopologyTestDriver testDriver = new TopologyTestDriver(eventTopologySupplier.get(), props)) {
            TestInputTopic<String, String> inputTopic = testDriver.createInputTopic(INPUT_TOPIC_NAME,
                    new StringSerializer(),
                    new StringSerializer());
            inputTopic.pipeInput("{\"hello\": \"viafoura\"}");
            verify(objectMapper, times(1)).readValue(any(String.class), any(TypeReference.class));
            verify(eventConsumer, times(1)).consumeEvent(any());
            verify(config, times(2)).getIncomingTopics();
        }
    }
}