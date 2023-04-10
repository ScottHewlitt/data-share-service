package com.viafoura.template.microservice.vertx.error;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_IMPLEMENTED;
import static io.netty.handler.codec.http.HttpResponseStatus.REQUEST_TIMEOUT;
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static org.mockito.Mockito.*;

import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import java.io.IOException;
import org.junit.jupiter.api.*;

class RoutingErrorHandlerTest {

    private final RoutingContext routingContext = mock(RoutingContext.class);
    private final HttpServerResponse httpServerResponse = mock(HttpServerResponse.class, RETURNS_DEEP_STUBS);

    @BeforeEach
    void setup() {
        when(routingContext.response()).thenReturn(httpServerResponse);
    }

    @Test
    void givenNullFailure_whenHandle_thenInternalServerError() {
        when(routingContext.failure()).thenReturn(null);
        when(routingContext.statusCode()).thenReturn(INTERNAL_SERVER_ERROR.code());
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(INTERNAL_SERVER_ERROR.code());
    }

    @Test
    void givenNullFailureWithUnauthorizedStatusCode_whenHandle_thenUnauthorizedError() {
        when(routingContext.failure()).thenReturn(null);
        when(routingContext.statusCode()).thenReturn(UNAUTHORIZED.code());
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(UNAUTHORIZED.code());
    }

    @Test
    void givenFailureReplyNoHandlers_whenHandle_thenNotImplementedError() {
        when(routingContext.failure()).thenReturn(new ReplyException(ReplyFailure.NO_HANDLERS));
        when(routingContext.statusCode()).thenReturn(-1);
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(NOT_IMPLEMENTED.code());
    }

    @Test
    void givenFailureReplyTimeOut_whenHandle_thenTimeOutError() {
        when(routingContext.failure()).thenReturn(new ReplyException(ReplyFailure.TIMEOUT));
        when(routingContext.statusCode()).thenReturn(-1);
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(REQUEST_TIMEOUT.code());
    }

    @Test
    void givenFailureGenericReply_whenHandle_thenInternalServerError() {
        when(routingContext.failure()).thenReturn(new ReplyException(ReplyFailure.ERROR));
        when(routingContext.statusCode()).thenReturn(-1);
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(INTERNAL_SERVER_ERROR.code());
    }

    @Test
    void givenFailureIllegalArgument_whenHandle_thenBadRequestError() {
        when(routingContext.failure()).thenReturn(new IllegalArgumentException());
        when(routingContext.statusCode()).thenReturn(-1);
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(BAD_REQUEST.code());
    }

    @Test
    void givenFailureGenericException_whenHandle_thenInternalServerError() {
        when(routingContext.failure()).thenReturn(new IOException());
        when(routingContext.statusCode()).thenReturn(-1);
        new RoutingErrorHandler().handle(routingContext);
        verify(httpServerResponse, times(1)).setStatusCode(INTERNAL_SERVER_ERROR.code());
    }
}