package com.romeao.bookstore.errorhandling;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final ApiError error;

    public ApiException(ApiError error) {
        this.error = error;
    }

    public ApiException(HttpStatus httpStatus, String message) {
        error = new ApiError(httpStatus, message);
    }

    public ApiError getError() {
        return error;
    }
}
