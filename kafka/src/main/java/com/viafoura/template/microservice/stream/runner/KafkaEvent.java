package com.viafoura.template.microservice.stream.runner;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize
public class KafkaEvent {

    private Map<String, Object> actualEvent;

    @Override
    public String toString() {
        return actualEvent.toString();
    }
}
