package com.romeao.bookstore.errorhandling;

import com.romeao.bookstore.api.v1.util.ErrorMessages;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldValidator {

    private static final String LIMIT = "limit";
    private static final String PAGE_NUM = "pageNum";

    private static boolean isValidInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<ApiValidationError> validateIntFields(Map<String, String> fields) {
        List<ApiValidationError> result = new ArrayList<>();

        fields.forEach((fieldName, fieldVal) -> {
            if (!isValidInt(fieldVal)) {
                result.add(new ApiValidationError(fieldName,
                        ErrorMessages.INVALID_INTEGER, fieldName));
            }
        });
        return result;
    }

    public static int validateIntField(String fieldName, String fieldVal) {
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldName, fieldVal);
        List<ApiValidationError> result = validateIntFields(fields);
        if (!result.isEmpty()) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INTEGER);
            error.getSubErrors().addAll(result);
            throw new ApiException(error);
        }
        return Integer.parseInt(fieldVal);
    }

    public static List<ApiValidationError> validatePageParameters(String limit, String pageNum) {
        Map<String, String> fields = new HashMap<>();
        fields.put(LIMIT, limit);
        fields.put(PAGE_NUM, pageNum);
        List<ApiValidationError> result = validateIntFields(fields);

        // Confirm that limit is at least 1
        if (isValidInt(limit)) {
            int intLimit = Integer.parseInt(limit);
            if (intLimit < 1) {
                result.add(new ApiValidationError(LIMIT, ErrorMessages.BAD_LIMIT_VALUE, limit));
            }
        }

        return result;
    }

    public static void doPageValidation(String limit, String pageNum) {
        List<ApiValidationError> errors =
                validatePageParameters(limit, pageNum);
        if (!errors.isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_REQUEST_PARAMETERS);
            apiError.getSubErrors().addAll(errors);
            throw new ApiException(apiError);
        }
    }
}
