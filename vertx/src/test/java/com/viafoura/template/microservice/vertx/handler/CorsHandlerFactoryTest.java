package com.viafoura.template.microservice.vertx.handler;

import static org.junit.jupiter.api.Assertions.*;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.*;

class CorsHandlerFactoryTest {

    @Test
    void givenDefaultConfiguration_whenLoad_thenSuccess() {
        CorsHandlerConfiguration configuration = new CorsHandlerFactory.DefaultCorsHandlerConfiguration();
        assertDefaultCorsConfiguration(configuration);
    }

    @Test
    void givenInstance_whenGetInstance_thenGetDefault() {
        assertNotNull(CorsHandlerFactory.createInstance());
        assertNotNull(CorsHandlerFactory.createInstance(new CorsHandlerFactory.DefaultCorsHandlerConfiguration()));
    }

    void assertDefaultCorsConfiguration(CorsHandlerConfiguration corsHandlerConfiguration) {
        assertTrue(corsHandlerConfiguration.getAllowCredentials());
        assertEquals(".*", corsHandlerConfiguration.getAllowedOriginPattern());
        assertEquals(12 * 60 * 60, corsHandlerConfiguration.getMaxAgeSeconds());
        assertEquals(new HashSet<>(Arrays.asList(
                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.PATCH,
                HttpMethod.DELETE
        )), corsHandlerConfiguration.getAllowedMethods());
        assertEquals(new HashSet<>(Arrays.asList(
                HttpHeaders.CONTENT_TYPE.toString(),
                HttpHeaders.AUTHORIZATION.toString(),
                "X-REQUEST-SIGNATURE"
        )), corsHandlerConfiguration.getAllowedHeaders());
    }
}
