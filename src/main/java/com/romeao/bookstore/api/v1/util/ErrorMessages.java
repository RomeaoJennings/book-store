package com.romeao.bookstore.api.v1.util;

public final class ErrorMessages {
    public static final String INVALID_INTEGER = "Field is not a valid integer.";
    public static final String PARAM_MUST_BE_POSITIVE = "Parameter must be a positive value.";
    public static final String INVALID_REQUEST_PARAMETERS = "Invalid request parameters.";
    public static final String RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String MALFORMED_PARAMETER = "Malformed parameter.";
    public static final String CANNOT_DELETE_RESOURCE = "The resource cannot be deleted because " +
            "it is being used by other entities.";
    public static final String RESOURCE_EXISTS = "The resource already exists.";
    public static final String FIELD_REQUIRED = "This field is required.";

    private ErrorMessages() {}
}
