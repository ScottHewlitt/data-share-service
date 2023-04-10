package com.viafoura.template.microservice.vertx.config;

import com.typesafe.config.Config;
import javax.inject.Inject;
import lombok.Getter;

@Getter
public class VertxConfigImpl implements VertxConfig {

    private final int serverPort;

    @Inject
    public VertxConfigImpl(Config config) {
        this.serverPort = config.getInt("service.server.port");
    }
}
