package com.viafoura.template.microservice.vertx.api.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @implSpec used as a POJO to deliver an error / error payload through vert.x to the caller
 * @implNote each fieldname is important, as vert.x deserializes this into a JsonObject and extracts by the fieldnames
 * @implNote payload should be null or something serializable into a base64 string - in the generated code we serialize
 * the payload in this way, as vert.x will take the payload, base64 decode, and send it as a buffer to the caller
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ApiException extends Exception {

    private final Map<String, String> headers;
    private final int statusCode;
    private final String statusMessage;
    private final transient Object payload;

    /**
     * @param statusCode - HTTP status code (eg. 400)
     * @param statusMessage - HTTP status message (eg. Bad Request)
     * @param payload - nullable payload to be serialized into a base64 string
     */
    public ApiException(int statusCode, String statusMessage, Object payload) {
        /*
         * we were adding this to all routes before, but it seems more appropriate in the content being delivered
         * if the headers needs to change programmatically, updating this class' interface will be required
         */
        this.headers = new HashMap<>(1);
        headers.put("Content-Type", "application/json; charset=utf-8");
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.payload = payload;
    }

    public ApiException(HttpResponseStatus status, Object payload) {
        this.headers = new HashMap<>(1);
        headers.put("Content-Type", "application/json; charset=utf-8");
        this.statusCode = status.code();
        this.statusMessage = status.reasonPhrase();
        this.payload = payload;
    }

}
