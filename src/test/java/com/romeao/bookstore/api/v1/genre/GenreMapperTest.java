package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapperTest {
    private static final Integer ID = 1;
    private static final String NAME = "Genre";

    private static final GenreMapper mapper = GenreMapper.INSTANCE;

    @Test
    void toEntity_withNullDto() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_withNullEntity() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toEntity() {
        // given
        GenreDto dto = new GenreDto(ID, NAME);

        // when
        Genre entity = mapper.toEntity(dto);

        // then
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(NAME, entity.getName());
    }

    @Test
    void toDto() {
        // given
        Genre entity = Genre.builder().id(ID).name(NAME).build();

        // when
        GenreDto dto = mapper.toDto(entity);

        // then
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(NAME, dto.getName());
    }

}