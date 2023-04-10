package com.viafoura.template.microservice.business.domain.event;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StreamEvent {

    Map<String, Object> values;

    public void addCustomValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void markAsVerified() {
        addCustomValue("verified", true);
    }

}
