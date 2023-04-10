package com.viafoura.template.microservice.vertx.handler;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CorsHandlerFactory {

    private CorsHandlerFactory() {
    }

    public static CorsHandler createInstance() {
        return createInstance(new DefaultCorsHandlerConfiguration());
    }

    public static CorsHandler createInstance(CorsHandlerConfiguration configuration) {
        return CorsHandler.create()
                .addRelativeOrigin(configuration.getAllowedOriginPattern())
                .allowedMethods(configuration.getAllowedMethods())
                .allowedHeaders(configuration.getAllowedHeaders())
                .allowCredentials(configuration.getAllowCredentials())
                .maxAgeSeconds(configuration.getMaxAgeSeconds());
    }

    public static final class DefaultCorsHandlerConfiguration implements CorsHandlerConfiguration {
        
        @Override
        public String getAllowedOriginPattern() {
            return ".*";
        }

        @Override
        public Set<HttpMethod> getAllowedMethods() {
            return new HashSet<>(Arrays.asList(
                    HttpMethod.GET,
                    HttpMethod.POST,
                    HttpMethod.PUT,
                    HttpMethod.PATCH,
                    HttpMethod.DELETE
            ));
        }

        @Override
        public Set<String> getAllowedHeaders() {
            return new HashSet<>(Arrays.asList(
                    HttpHeaders.CONTENT_TYPE.toString(),
                    HttpHeaders.AUTHORIZATION.toString(),
                    /* Viafoura custom header for v3 content syndication */
                    "X-REQUEST-SIGNATURE"
            ));
        }

        @Override
        public boolean getAllowCredentials() {
            return true;
        }

        @Override
        public int getMaxAgeSeconds() {
            return 12 * 60 * 60;
        }
    }
}
