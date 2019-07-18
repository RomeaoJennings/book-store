package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.domain.Book;
import com.romeao.bookstore.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BookBuilderTest {

    private static final Integer ID = 2334;
    private static final String NAME = "Cracking the Coding Interview";
    private static final String PUBLISHER = "Career Cup, LLC";
    private static final double PRICE = 29.99;

    private static final Integer AUTHOR_ID = 1;
    private static final String AUTHOR_FIRST = "Gayle";
    private static final String AUTHOR_LAST = "McDowell";

    private static final Integer GENRE_ID = 1;
    private static final String GENRE_NAME = "Interview Books";

    private static Author author;
    private static Genre genre;

    @BeforeEach
    void setUp() {
        genre = Genre.builder()
                .id(GENRE_ID)
                .name(GENRE_NAME)
                .build();

        author = Author.builder()
                .id(AUTHOR_ID)
                .firstName(AUTHOR_FIRST)
                .lastName(AUTHOR_LAST)
                .build();
    }

    @Test
    void testBuild_withAllFields() {
        // when
        Book book = Book.builder()
                .id(ID)
                .name(NAME)
                .price(PRICE)
                .publisher(PUBLISHER)
                .authors(author)
                .genres(genre)
                .build();

        // then
        assertNotNull(book);
        assertEquals(ID, book.getId());
        assertEquals(NAME, book.getName());
        assertEquals(0, book.getPrice().compareTo(BigDecimal.valueOf(PRICE)));
        assertEquals(PUBLISHER, book.getPublisher());
        assertEquals(1, book.getAuthors().size());
        assertTrue(book.getAuthors().contains(author));
        assertEquals(ID, author.getBooks().iterator().next().getId());
        assertEquals(1, book.getGenres().size());
        assertTrue(book.getGenres().contains(genre));
    }

    @Test
    void testWithName() {
        // when
        Book book = Book.builder().name(NAME).build();

        // then
        assertNotNull(book);
        assertEquals(NAME, book.getName());
    }

    @Test
    void testWithPrice() {
        // when
        Book book = Book.builder().price(PRICE).build();

        // then
        assertNotNull(book);
        assertEquals(0, book.getPrice().compareTo(BigDecimal.valueOf(PRICE)));
    }

    @Test
    void withPublisher() {
        // when
        Book book = Book.builder().publisher(PUBLISHER).build();

        // then
        assertNotNull(book);
        assertEquals(PUBLISHER, book.getPublisher());
    }

    @Test
    void withGenres() {
        // when
        Book book = Book.builder().genres(genre).build();

        // then
        assertNotNull(book);
        assertEquals(1, book.getGenres().size());
        assertTrue(book.getGenres().contains(genre));
    }

    @Test
    void withAuthors() {
        // when
        Book book = Book.builder().authors(author).build();

        // then
        assertNotNull(book);
        assertEquals(1, book.getAuthors().size());
        assertTrue(book.getAuthors().contains(author));
        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(book));
    }
}