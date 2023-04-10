package com.viafoura.template.microservice.vertx.error;

import io.vertx.core.json.JsonObject;

public record ErrorResponse(Throwable error) {

    public static final String ERROR_KEY = "error";

    public JsonObject toJson() {
        return new JsonObject()
                .put(ERROR_KEY, error.getMessage());
    }
}
