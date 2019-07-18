package com.romeao.bookstore.repositories;

import com.romeao.bookstore.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class AuthorRepositoryIT {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void beansAreNotNull() {
        assertNotNull(authorRepository);
    }

    @Test
    void testRepositoryLoadsRelatedEntities() {
        Author author = authorRepository.findById(1).orElse(null);

        assertNotNull(author);
        assertNotEquals(0, author.getBooks().size());
    }
}