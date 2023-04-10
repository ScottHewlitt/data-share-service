package com.viafoura.template.microservice.stream.runner.rx;

import static org.mockito.Mockito.*;

import com.viafoura.template.microservice.stream.metric.KafkaStreamMonitorMetrics;
import com.viafoura.template.microservice.stream.runner.KafkaStreamSource;
import org.apache.kafka.streams.KafkaStreams.State;
import org.junit.jupiter.api.*;

class KafkaStreamSourceMonitorTest {

    private final KafkaStreamMonitorMetrics metrics = mock(KafkaStreamMonitorMetrics.class);
    private final KafkaStreamSource kafkaStreamSource = mock(KafkaStreamSource.class);
    private KafkaStreamSourceMonitor kafkaStreamSourceMonitor;

    @BeforeEach
    void setup() {
        kafkaStreamSourceMonitor = new KafkaStreamSourceMonitor(metrics, kafkaStreamSource);
    }

    @Test
    void givenKafkaStreamSourceMonitor_whenStart_thenKeepingAlive() {
        when(kafkaStreamSource.state()).thenReturn(State.RUNNING);
        kafkaStreamSourceMonitor.start();
        verify(metrics, times(1)).kafkaStreamCreate();
        verify(metrics, times(0)).kafkaStreamFailure();
        kafkaStreamSourceMonitor.stop();
    }
}
