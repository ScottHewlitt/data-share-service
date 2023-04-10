package com.viafoura.template.microservice.stream.runner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.function.Supplier;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;
import org.junit.jupiter.api.*;

class KafkaStreamsSourceTest {

    private final Supplier<KafkaStreams> kafkaStreamsSupplier = mock(Supplier.class);
    private KafkaStreams kafkaStreams = mock(KafkaStreams.class);
    private KafkaStreamsSource kafkaStreamsSource;

    @BeforeEach
    void setup() {
        when(kafkaStreamsSupplier.get()).thenReturn(kafkaStreams);
        kafkaStreamsSource = new KafkaStreamsSource(kafkaStreamsSupplier);
    }

    @Test
    void givenStreamsSource_whenStart_thenSuccess() {
        kafkaStreamsSource.start();
        verify(kafkaStreams, times(1)).start();
    }

    @Test
    void givenStreamsSource_whenGetState_thenAvailableState() {
        when(kafkaStreams.state()).thenReturn(State.RUNNING);
        kafkaStreamsSource.start();
        assertNotNull(kafkaStreamsSource.state());
    }

    @Test
    void givenStreamsSource_whenClose_thenSuccess() {
        kafkaStreamsSource.start();
        kafkaStreamsSource.close();
        verify(kafkaStreams, times(1)).close();
    }

    @Test
    void givenStreamsSource_whenCleanup_thenSuccess() {
        kafkaStreamsSource.start();
        kafkaStreamsSource.cleanUp();
        verify(kafkaStreams, times(1)).cleanUp();
    }
}
