package com.romeao.bookstore.repositories;

import com.romeao.bookstore.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class JpaRepositoryIT {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void beansAreNotNull() {
        assertNotNull(authorRepository);
        assertNotNull(bookRepository);
    }

    @Test
    void testBookRepositoryLoadsRelatedEntities() {
        // when
        Book book = bookRepository.findById(1).orElse(null);

        // then
        assertNotNull(book);
        assertNotEquals(0, book.getAuthors().size());
        assertNotEquals(0, book.getGenres().size());
    }
}