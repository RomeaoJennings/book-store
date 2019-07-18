package com.romeao.bookstore.repositories;

import com.romeao.bookstore.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookRepositoryIT {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testRepositoryLoads() {
        assertNotNull(bookRepository);
    }

    @Test
    void testRepositoryLoadsRelatedEntities() {
        // when
        Book book = bookRepository.findById(1).orElse(null);

        // then
        assertNotNull(book);
        assertNotEquals(0, book.getAuthors().size());
        assertNotEquals(0, book.getGenres().size());
    }
}