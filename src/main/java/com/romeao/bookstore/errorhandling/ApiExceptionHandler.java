package com.romeao.bookstore.errorhandling;

import com.romeao.bookstore.api.v1.util.ErrorMessages;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException exception) {
        ApiError error = exception.getError();
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConstraintExceptions(DataIntegrityViolationException ex) {
        ApiError error = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorMessages.CANNOT_DELETE_RESOURCE, ex.toString());
        return new ResponseEntity<>(error, error.getStatus());
    }
}
