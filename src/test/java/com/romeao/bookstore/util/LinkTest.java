package com.romeao.bookstore.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LinkTest {
    private static final String NAME = "self";
    private static final String URL = "/api/v1/genres/1";

    @Test
    void testLink() {
        // when
        Link link = new Link(NAME, URL);

        // then
        assertNotNull(link);
        assertEquals(NAME, link.getName());
        assertEquals(URL, link.getUrl());
    }
}