package com.viafoura.template.microservice;


import com.viafoura.template.microservice.di.util.GuiceInjectorFactory;
import com.viafoura.template.microservice.vertx.VertxApplicationLauncher;

public class Main {

    public static void main(String[] args) {
        new VertxApplicationLauncher().run(GuiceInjectorFactory.getOrCreateInjector());
    }

}
