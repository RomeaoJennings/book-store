package com.romeao.bookstore.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
    private static final String NAME = "self";
    private static final String URL = "/api/v1/genres/1";

    private static final Link LINK = new Link(NAME, URL);
    @Test
    void testLink() {
        // WHEN
        Link link = new Link(NAME, URL);

        // THEN
        assertNotNull(link);
        assertEquals(NAME, link.getName());
        assertEquals(URL, link.getUrl());
    }

    @Test
    void testLink_withNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Link(null, URL));
    }

    @Test
    void testLink_withNullURL() {
        assertThrows(IllegalArgumentException.class, () -> new Link(NAME, null));
    }

    @Test
    void testEquals_againstNull() {
        assertNotEquals(LINK, null);
    }

    @Test
    void testEquals_withSameObject() {
        assertEquals(LINK, LINK);
    }

    @Test
    void testEquals_withDifferentClass() {
        assertNotEquals(LINK, 1);
    }

    @Test
    void testEquals_withDifferentName() {
        // GIVEN
        Link link2 = new Link("name2", URL);

        // THEN
        assertNotEquals(LINK, link2);
    }

    @Test
    void testEquals_withDifferentUrl() {
        // GIVEN
        Link link2 = new Link(NAME, "url2");

        // THEN
        assertNotEquals(LINK, link2);
    }
}