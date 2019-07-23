package com.romeao.bookstore.errorhandling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"message", "status", "statusCode", "timestamp", "validationErrors"})
public class ApiError {
    private final HttpStatus status;
    private final LocalDateTime timestamp;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ApiValidationError> validationErrors = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String debugMessage;

    public ApiError(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.debugMessage = null;
    }

    public ApiError(HttpStatus status, String message, String debugMessage) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    // Only used by Jackson to Deserialize to object
    @JsonCreator
    private ApiError(@JsonProperty("status") HttpStatus status,
                     @JsonProperty("timestamp") LocalDateTime timestamp,
                     @JsonProperty("message") String message,
                     @JsonProperty("debugMessage") String debugMessage) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public int getStatusCode() {
        return status.value();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<ApiValidationError> getValidationErrors() {
        return validationErrors;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
