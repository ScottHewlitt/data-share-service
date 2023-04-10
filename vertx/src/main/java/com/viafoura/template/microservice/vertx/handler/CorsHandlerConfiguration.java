package com.viafoura.template.microservice.vertx.handler;

import io.vertx.core.http.HttpMethod;
import java.util.Set;

public interface CorsHandlerConfiguration {
    String getAllowedOriginPattern();
    Set<HttpMethod> getAllowedMethods();
    Set<String> getAllowedHeaders();
    boolean getAllowCredentials();
    int getMaxAgeSeconds();
}
