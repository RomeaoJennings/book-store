package com.romeao.bookstore.api.v1.mappers;

import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import com.romeao.bookstore.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreSummaryMapperTest {
    private static final String NAME = "Genre";

    private static final GenreSummaryMapper mapper = GenreSummaryMapper.INSTANCE;

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
        GenreSummaryDto dto = new GenreSummaryDto();
        dto.setName(NAME);

        // when
        Genre entity = mapper.toEntity(dto);

        // then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(NAME, entity.getName());
    }

    @Test
    void toDto() {
        // given
        Genre entity = Genre.builder().name(NAME).build();

        // when
        GenreSummaryDto dto = mapper.toDto(entity);

        // then
        assertNotNull(dto);
        assertEquals(NAME, dto.getName());
    }

}