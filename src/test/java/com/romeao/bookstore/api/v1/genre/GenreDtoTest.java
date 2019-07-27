package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GenreDtoTest {
    private static final String NAME = "name";
    private static final int ID = 1;

    private GenreDto dto;

    @BeforeEach
    void setUp() {
        dto = new GenreDto(ID, NAME);
        dto.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(ID)));
    }

    @Test
    void getName() {
        assertEquals(NAME, dto.getName());
    }

    @Test
    void setName() {
        final String newName = "newName";
        dto.setName(newName);
        assertEquals(newName, dto.getName());
    }

    @Test
    void equals_withSameObject() {
        assertEquals(dto, dto);
    }

    @Test
    void equals_withNull() {
        assertNotEquals(dto, null);
    }

    @Test
    void equals_withDifferentObjectType() {
        int comparator = 1;
        assertNotEquals(dto, comparator);
    }

    @Test
    void equals_withDifferentIds() {
        GenreDto dto2 = new GenreDto(NAME);
        assertNotEquals(dto2, dto);
        assertNotEquals(dto, dto2);
    }

    @Test
    void equals_withDifferentNames() {
        dto.setId(null);
        GenreDto dto2 = new GenreDto("name2");
        assertNotEquals(dto, dto2);
    }

    @Test
    void equals_withDifferentLinkSize() {
        dto.setId(null);
        GenreDto dto2 = new GenreDto(NAME);
        assertNotEquals(dto, dto2);
    }

    @Test
    void equals_withDifferentLinks() {
        dto.setId(null);
        GenreDto dto2 = new GenreDto(NAME);
        dto2.getLinks().add(new Link(LinkNames.SELF, "differentUrl"));
        assertNotEquals(dto, dto2);
    }
}