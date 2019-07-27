package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.domain.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {
    private static final Integer ID = 1;
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
        AuthorDto dto = new AuthorDto(ID, FIRST, LAST);

        // when
        Author entity = mapper.toEntity(dto);

        // then
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(FIRST, entity.getFirstName());
        assertEquals(LAST, entity.getLastName());
    }

    @Test
    void toDto() {
        // given
        Author entity = Author.builder().id(ID).firstName(FIRST).lastName(LAST).build();

        // when
        AuthorDto dto = mapper.toDto(entity);

        // then
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(FIRST, dto.getFirstName());
        assertEquals(LAST, dto.getLastName());
    }

}