package com.viafoura.template.microservice.stream.runner.consumer;

import java.util.function.Supplier;
import org.apache.kafka.streams.Topology;

@FunctionalInterface
public interface TopologySupplier extends Supplier<Topology> {
}
