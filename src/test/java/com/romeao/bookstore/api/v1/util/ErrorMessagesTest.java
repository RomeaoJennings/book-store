package com.romeao.bookstore.api.v1.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorMessagesTest extends ErrorMessages {

    @Test
    void testHasProtectedConstructor() throws Exception {
        Constructor constructor = ErrorMessages.class.getDeclaredConstructors()[0];
        assertTrue(Modifier.isProtected(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

}