package com.romeao.bookstore.api.v1.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorMessagesTest {
    @Test
    void testHasPrivateConstructor() throws Exception {
        Constructor constructor = ErrorMessages.class.getDeclaredConstructors()[0];
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

}