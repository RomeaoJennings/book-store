package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.domain.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorBuilderTest {

    private static final Integer ID = 37475;
    private static final String FIRST = "Steven";
    private static final String LAST = "King";

    private static final Integer BOOK_ID = 1;
    private static final String BOOK_NAME = "It";
    private static final Book BOOK = Book.builder().withId(BOOK_ID).withName(BOOK_NAME).build();

    @Test
    void testAllFields() {
        // when
        Author author = Author.builder()
                .withId(ID)
                .withFirstName(FIRST)
                .withLastName(LAST)
                .withBooks(BOOK)
                .build();

        // then
        assertNotNull(author);
        assertEquals(ID, author.getId());
        assertEquals(FIRST, author.getFirstName());
        assertEquals(LAST, author.getLastName());
        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(BOOK));
    }

    @Test
    void testWithId() {
        // when
        Author author = Author.builder().withId(ID).build();

        // then
        assertNotNull(author);
        assertEquals(ID, author.getId());
        assertNull(author.getFirstName());
        assertNull(author.getLastName());
    }

    @Test
    void testWithFirstName() {
        // when
        Author author = Author.builder().withFirstName(FIRST).build();

        // then
        assertNotNull(author);
        assertNull(author.getId());
        assertEquals(FIRST, author.getFirstName());
        assertNull(author.getLastName());
    }

    @Test
    void testWithLastName() {
        // when
        Author author = Author.builder().withLastName(LAST).build();

        // then
        assertNotNull(author);
        assertNull(author.getId());
        assertNull(author.getFirstName());
        assertEquals(LAST, author.getLastName());
    }

    @Test
    void testWithBooks() {
        // when
        Author author = Author.builder().withBooks(BOOK).build();

        // then
        assertNotNull(author);
        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(BOOK));
    }
}