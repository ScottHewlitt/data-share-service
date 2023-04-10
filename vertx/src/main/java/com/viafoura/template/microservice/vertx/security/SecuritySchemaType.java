package com.viafoura.template.microservice.vertx.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecuritySchemaType {

    TOKEN("Token"),
    TOKEN_IN_COOKIE("TokenInCookie");

    private final String name;

}
