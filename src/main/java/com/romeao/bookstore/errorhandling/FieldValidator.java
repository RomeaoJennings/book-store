package com.romeao.bookstore.errorhandling;

import com.romeao.bookstore.api.v1.util.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldValidator {

    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUM = "pageNumber";

    private static boolean isValidInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void validateIntFields(Map<String, String> fields) {
        List<ApiValidationError> errors = new ArrayList<>();

        fields.forEach((fieldName, fieldVal) -> {
            if (!isValidInt(fieldVal)) {
                errors.add(new ApiValidationError(fieldName,
                        ErrorMessages.INVALID_INTEGER, fieldVal));
            }
        });
        if (!errors.isEmpty()) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
                    ErrorMessages.MALFORMED_PARAMETER);
            error.getSubErrors().addAll(errors);
            throw new ApiException(error);
        }
    }

    public static int validateIntField(String fieldName, String fieldValue) {
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldName, fieldValue);
        validateIntFields(fields);
        return Integer.parseInt(fieldValue);
    }

    public static void doPageValidation(String pageSize, String pageNumber) {
        Map<String, String> fields = new HashMap<>();
        fields.put(PAGE_SIZE, pageSize);
        fields.put(PAGE_NUM, pageNumber);

        // validate values for integers that cannot be parsed
        validateIntFields(fields);

        // Confirm that pageSize is positive
        if (Integer.parseInt(pageSize) < 1) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_REQUEST_PARAMETERS);
            error.getSubErrors().add(new ApiValidationError(PAGE_SIZE,
                    ErrorMessages.PARAM_MUST_BE_POSITIVE, pageSize));
            throw new ApiException(error);
        }
    }

    public static void doFieldValidation(BindingResult validation) {
        if (validation.hasFieldErrors()) {
            List<ApiValidationError> fieldErrors = new ArrayList<>();
            validation.getFieldErrors().forEach(fieldError ->
                    fieldErrors.add(new ApiValidationError(
                            fieldError.getField(),
                            fieldError.getDefaultMessage(),
                            fieldError.getRejectedValue())));
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_REQUEST_PARAMETERS);
            error.getSubErrors().addAll(fieldErrors);
            throw new ApiException(error);
        }
    }
}
