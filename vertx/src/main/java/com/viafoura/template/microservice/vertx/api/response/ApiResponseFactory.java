package com.viafoura.template.microservice.vertx.api.response;

import io.vertx.core.json.Json;
import java.util.Base64;
import java.util.Objects;

/**
 * simpler creation methods for standard HttpResponses where status code and status message are known
 * @see ApiResponse
 */
public final class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    public static <T> ApiResponse<T> created(T payload) { return new ApiResponse<>(201, "Created", payload); }

    public static <T> ApiResponse<T> ok(T payload) {
        return new ApiResponse<>(200, "OK", payload);
    }

    public static <T> ApiResponse<T> accepted(T payload) {
        return new ApiResponse<>(202, "Accepted", payload);
    }

    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(204, "No Content", null);
    }

    public static ApiResponse<String> encodeResponse(ApiResponse<?> apiResponse) {
        return new ApiResponse<>(
                apiResponse.getStatusCode(),
                apiResponse.getStatusMessage(),
                                    Objects.isNull(apiResponse.getPayload()) ? null :
                                            Base64.getEncoder().encodeToString(Json.encode(apiResponse.getPayload()).getBytes())
                            );
    }
}
