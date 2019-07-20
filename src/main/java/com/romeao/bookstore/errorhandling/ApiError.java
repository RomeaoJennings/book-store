package com.romeao.bookstore.errorhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"message", "status", "statusCode", "timestamp", "subErrors"})
public class ApiError {
    private final HttpStatus status;
    private final LocalDateTime timestamp;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ApiSubError> subErrors = new ArrayList<>();

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

    public HttpStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return status.value();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
