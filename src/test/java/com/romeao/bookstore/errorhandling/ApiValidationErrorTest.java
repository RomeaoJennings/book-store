package com.romeao.bookstore.errorhandling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ApiValidationErrorTest {

    private static final String MESSAGE = "message";
    private static final String FIELD = "field";
    private static final String REJECTED_VAL = "val";

    private ApiValidationError error;

    @BeforeEach
    void setUp() {
        error = new ApiValidationError(FIELD, MESSAGE, REJECTED_VAL);
    }

    @Test
    void getField() {
        assertEquals(FIELD, error.getField());
    }

    @Test
    void getRejectedValue() {
        assertEquals(REJECTED_VAL, error.getRejectedValue());
    }

    @Test
    void getMessage() {
        assertEquals(MESSAGE, error.getMessage());
    }

    @Test
    void equals_withSameObject() {
        assertEquals(error, error);
    }

    @Test
    void equals_withNull() {
        assertNotEquals(error, null);
    }

    @Test
    void equals_withDifferentClassType() {
        assertNotEquals(error, 1);
    }

    @Test
    void equals_withDifferentField() {
        // GIVEN
        ApiValidationError error2 = new ApiValidationError("field2", MESSAGE, REJECTED_VAL);

        // THEN
        assertNotEquals(error, error2);
    }

    @Test
    void equals_withDifferentMessage() {
        // GIVEN
        ApiValidationError error2 = new ApiValidationError(FIELD, "message2", REJECTED_VAL);

        // THEN
        assertNotEquals(error, error2);
    }

    @Test
    void equals_withDifferentRejectedValue() {
        // GIVEN
        ApiValidationError error2 = new ApiValidationError(FIELD, MESSAGE, "rejectedVal2");

        // THEN
        assertNotEquals(error, error2);
    }

    @Test
    void equals_withSameParameters() {
        // GIVEN
        ApiValidationError error2 = new ApiValidationError(FIELD, MESSAGE, REJECTED_VAL);

        // THEN
        assertEquals(error, error2);
    }

    @Test
    void hashCodeTest() {
    }
}