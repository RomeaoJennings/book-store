package com.romeao.bookstore.errorhandling;

import com.romeao.bookstore.api.v1.util.ErrorMessages;
import org.springframework.http.HttpStatus;

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

    private static List<ApiValidationError> validateIntFields(Map<String, String> fields) {
        List<ApiValidationError> result = new ArrayList<>();

        fields.forEach((fieldName, fieldVal) -> {
            if (!isValidInt(fieldVal)) {
                result.add(new ApiValidationError(fieldName,
                        ErrorMessages.INVALID_INTEGER, fieldVal));
            }
        });
        return result;
    }

    public static int validateIntField(String fieldName, String fieldValue) {
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldName, fieldValue);
        List<ApiValidationError> result = validateIntFields(fields);
        if (!result.isEmpty()) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INTEGER);
            error.getSubErrors().addAll(result);
            throw new ApiException(error);
        }
        return Integer.parseInt(fieldValue);
    }

    private static List<ApiValidationError> validatePageParameters(String pageSize,
                                                                   String pageNumber) {
        Map<String, String> fields = new HashMap<>();
        fields.put(PAGE_SIZE, pageSize);
        fields.put(PAGE_NUM, pageNumber);
        List<ApiValidationError> result = validateIntFields(fields);

        // Confirm that pageSize is at least 1
        if (isValidInt(pageSize)) {
            int intLimit = Integer.parseInt(pageSize);
            if (intLimit < 1) {
                result.add(new ApiValidationError(PAGE_SIZE, ErrorMessages.PARAM_MUST_BE_POSITIVE
                        , pageSize));
            }
        }
        return result;
    }

    public static void doPageValidation(String pageSize, String pageNumber) {
        List<ApiValidationError> errors =
                validatePageParameters(pageSize, pageNumber);
        if (!errors.isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_REQUEST_PARAMETERS);
            apiError.getSubErrors().addAll(errors);
            throw new ApiException(apiError);
        }
    }
}
