package com.romeao.bookstore.api.v1.mappers;

import com.romeao.bookstore.api.v1.models.AuthorDto;
import com.romeao.bookstore.domain.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {
    private static final String FIRST = "First";
    private static final String LAST = "Last";

    private static final AuthorMapper mapper = AuthorMapper.INSTANCE;

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
        AuthorDto dto = new AuthorDto(FIRST, LAST);

        // when
        Author entity = mapper.toEntity(dto);

        // then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(FIRST, entity.getFirstName());
        assertEquals(LAST, entity.getLastName());
    }

    @Test
    void toDto() {
        // given
        Author entity = Author.builder().firstName(FIRST).lastName(LAST).build();

        // when
        AuthorDto dto = mapper.toDto(entity);

        // then
        assertNotNull(dto);
        assertEquals(FIRST, dto.getFirstName());
        assertEquals(LAST, dto.getLastName());
    }

}