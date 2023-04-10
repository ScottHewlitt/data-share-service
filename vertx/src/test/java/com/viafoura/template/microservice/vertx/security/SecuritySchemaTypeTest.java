package com.viafoura.template.microservice.vertx.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class SecuritySchemaTypeTest {

    @Test
    void givenSecuritySchemaType_whenConstruct_thenSuccess() {
        SecuritySchemaType securitySchemaTypeToken = SecuritySchemaType.TOKEN;
        assertEquals("Token", securitySchemaTypeToken.getName());
        SecuritySchemaType securitySchemaTypeTokenInCookie = SecuritySchemaType.TOKEN_IN_COOKIE;
        assertEquals("TokenInCookie", securitySchemaTypeTokenInCookie.getName());
    }
}
