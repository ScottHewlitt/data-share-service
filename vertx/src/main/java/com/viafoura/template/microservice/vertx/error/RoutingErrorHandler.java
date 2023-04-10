package com.viafoura.template.microservice.vertx.error;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoutingErrorHandler implements Handler<RoutingContext> {

    private static final int UNAUTHORIZED = 401;
    private static final int INVALID_STATUS_CODE = -1;
    private static final int DEFAULT_STATUS_CODE = 500;

    @Override
    public void handle(RoutingContext context) {
        Throwable failure = context.failure();
        int statusCode = resolveStatusCode(context);

        ErrorResponse response = new ErrorResponse(isFailureNull(failure) ? resolveException(statusCode) : failure);
        log.error(response.error().getMessage(), response.error());

        context.response()
                .setStatusCode(statusCode)
                .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .end(response.toJson().encodePrettily());
    }

    private int resolveStatusCode(RoutingContext context) {
        int statusCode = context.statusCode();
        if (isValidStatusCode(statusCode)) {
            return statusCode;
        }

        if (context.failure() instanceof ReplyException replyException) {
            switch (replyException.failureType()) {
                case NO_HANDLERS -> statusCode = HttpResponseStatus.NOT_IMPLEMENTED.code();
                case TIMEOUT -> statusCode = HttpResponseStatus.REQUEST_TIMEOUT.code();
                default -> statusCode = DEFAULT_STATUS_CODE;
            }
        } else if (context.failure() instanceof IllegalArgumentException) {
            statusCode = HttpResponseStatus.BAD_REQUEST.code();
        } else {
            statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
        }
        return statusCode;
    }

    private Throwable resolveException(int statusCode) {
        if (statusCode == UNAUTHORIZED) {
            return new Exception("Unauthorized");
        }
        log.error("Returning unknown exception due to status code {}", statusCode);
        return new Exception("Unknown exception");
    }

    private static boolean isFailureNull(Throwable throwable) {
        return throwable == null;
    }

    private static boolean isValidStatusCode(int statusCode) {
        return statusCode != INVALID_STATUS_CODE;
    }
}
