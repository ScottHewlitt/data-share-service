package com.viafoura.template.microservice.stream.runner.rx;

import com.viafoura.template.microservice.stream.metric.KafkaStreamMonitorMetrics;
import com.viafoura.template.microservice.stream.runner.KafkaStreamSource;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class KafkaStreamSourceMonitor {

    private final KafkaStreamMonitorMetrics metrics;
    private final KafkaStreamSource kafkaStreamSource;

    public void start() {
        Completable
                /* this is an eternal looping process barring exceptions or a KafkaStreams in an ERROR state */
                .defer(this::manageSingleKafkaStreamsSource)
                /* if an exception bubbles up we either have an uncaught exception or a KafkaStreams in an ERROR state */
                .doOnError(e -> log.warn("KafkaStreams is in an invalid state, recreating KafkaStream instance"))
                .doOnError(metrics::kafkaStreamFailure)
                /* this resubscribes on error so that we return to the eternal looping process but with a new KafkaStreams instance */
                .retryWhen(throwable -> throwable.delay(5, TimeUnit.SECONDS))
                .subscribe();
    }

    public void stop() {
        kafkaStreamSource.close();
        kafkaStreamSource.cleanUp();
    }

    private Completable manageSingleKafkaStreamsSource() {
        log.debug("Closing and cleaning up resources of previous instance of KafkaStreams");
        kafkaStreamSource.close();
        kafkaStreamSource.cleanUp();
        log.debug("Starting new KafkaStreams instance");
        metrics.kafkaStreamCreate();
        kafkaStreamSource.start();
        return Observable
                /* the same instance will emit on each repeat */
                .just(kafkaStreamSource)
                .mergeWith(Completable.timer(5, TimeUnit.SECONDS))
                /* we check the (single) instance to see if it's in an error state */
                .doOnNext(source -> log.trace("Checking Stream state: current state {}", source.state()))
                .doOnNext(source -> {
                    if (source.state() == KafkaStreams.State.ERROR) {
                        /* this will break through the repeat of this observable to a caller who can handle the error */
                        throw new IllegalStateException("KafkaStreams is in an error state, breaking out of repeating process");
                    }
                })
                .doOnNext(metrics::kafkaStreamHeartbeat)
                .ignoreElements()
                /* repeat endlessly (until an exception arises since we have not handled errors here) */
                .repeat();
    }

}
