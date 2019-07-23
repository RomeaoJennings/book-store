package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// TODO: Refactor this out of AuthorService by adding self links to DTOs

public abstract class AuthorTestUtils extends TestUtils {
    protected static void assertAuthorNameIsCorrect(AuthorDto dto, String firstName,
                                                    String lastName) {
        assertNotNull(dto);
        assertEquals(firstName, dto.getFirstName());
        assertEquals(lastName, dto.getLastName());
    }

    protected static void assertHasSelfLink(AuthorDto dto, int authorId) {
        assertCorrectSelfLink(dto, Endpoints.Author.byAuthorId(authorId));
    }
}
