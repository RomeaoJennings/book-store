package com.romeao.bookstore.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LinkNamesTest extends LinkNames {

    @Test
    void testHasProtectedConstructor() throws Exception {
        Constructor constructor = LinkNames.class.getDeclaredConstructors()[0];
        assertTrue(Modifier.isProtected(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}