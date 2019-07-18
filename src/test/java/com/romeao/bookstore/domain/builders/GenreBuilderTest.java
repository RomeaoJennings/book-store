package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreBuilderTest {
    private static final Integer ID = 1234;
    private static final String NAME = "Action";

    @Test
    void testAllFields() {
        // when
        Genre genre = Genre.builder().id(ID).name(NAME).build();

        // then
        assertNotNull(genre);
        assertEquals(ID, genre.getId());
        assertEquals(NAME, genre.getName());
    }

    @Test
    void testId() {
        // when
        Genre genre = Genre.builder().id(ID).build();

        // then
        assertNotNull(genre);
        assertEquals(ID, genre.getId());
        assertNull(genre.getName());
    }

    @Test
    void testName() {
        // when
        Genre genre = Genre.builder().name(NAME).build();

        // then
        assertNotNull(genre);
        assertNull(genre.getId());
        assertEquals(NAME, genre.getName());
    }
}