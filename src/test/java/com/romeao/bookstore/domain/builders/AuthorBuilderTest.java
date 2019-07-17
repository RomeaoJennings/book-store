package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorBuilderTest {

    private static final Integer ID = 37475;
    private static final String FIRST = "Steven";
    private static final String LAST = "King";

    @Test
    void testAllFields() {
        // when
        Author author = Author.builder().withId(ID).withFirstName(FIRST).withLastName(LAST).build();

        // then
        assertNotNull(author);
        assertEquals(ID, author.getId());
        assertEquals(FIRST, author.getFirstName());
        assertEquals(LAST, author.getLastName());
    }

    @Test
    void withId() {
        // when
        Author author = Author.builder().withId(ID).build();

        // then
        assertNotNull(author);
        assertEquals(ID, author.getId());
        assertNull(author.getFirstName());
        assertNull(author.getLastName());
    }

    @Test
    void withFirstName() {
        // when
        Author author = Author.builder().withFirstName(FIRST).build();

        // then
        assertNotNull(author);
        assertNull(author.getId());
        assertEquals(FIRST, author.getFirstName());
        assertNull(author.getLastName());
    }

    @Test
    void withLastName() {
        // when
        Author author = Author.builder().withLastName(LAST).build();

        // then
        assertNotNull(author);
        assertNull(author.getId());
        assertNull(author.getFirstName());
        assertEquals(LAST, author.getLastName());
    }
}