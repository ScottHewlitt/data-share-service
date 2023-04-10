package com.viafoura.template.microservice.business.port.output.metric;

public interface ApplicationMetricsPort {

    void incrementHealthyCalls();
    void incrementVerifiedEvents();

}
