package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthorDtoTest {
    private static final String FIRST = "first";
    private static final String LAST = "last";
    private static final int ID = 1;

    private AuthorDto dto;

    @BeforeEach
    void setUp() {
        dto = new AuthorDto(FIRST, LAST);
        dto.getLinks().add(new Link(LinkNames.SELF, Endpoints.Author.byAuthorId(ID)));
    }

    @Test
    void getFirstName() {
        assertEquals(FIRST, dto.getFirstName());
    }

    @Test
    void setFirstName() {
        final String first2 = "first2";
        dto.setFirstName(first2);
        assertEquals(first2, dto.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals(LAST, dto.getLastName());
    }

    @Test
    void setLastName() {
        final String last2 = "last2";
        dto.setLastName(last2);
        assertEquals(last2, dto.getLastName());
    }

    @Test
    void equals_sameObject() {
        assertEquals(dto, dto);
    }

    @Test
    void equals_withNull() {
        assertNotEquals(dto, null);
    }

    @Test
    void equals_withDifferentClass() {
        int comparator = 1;
        assertNotEquals(dto, comparator);
    }

    @Test
    void equals_differentFirstName() {
        AuthorDto dto2 = new AuthorDto("first2", LAST);
        assertNotEquals(dto, dto2);
    }

    @Test
    void equals_withDifferentLastName() {
        AuthorDto dto2 = new AuthorDto(FIRST, "last2");
        assertNotEquals(dto, dto2);
    }

    @Test
    void equals_withDifferentLinkSize() {
        AuthorDto dto2 = new AuthorDto(FIRST, LAST);
        assertNotEquals(dto, dto2);
    }

    @Test
    void equals_withDifferentLinks() {
        AuthorDto dto2 = new AuthorDto(FIRST, LAST);
        dto2.getLinks().add(new Link(LinkNames.SELF, "differentURL"));
        assertNotEquals(dto, dto2);
    }
}