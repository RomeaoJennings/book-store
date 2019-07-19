package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import com.romeao.bookstore.domain.Genre;
import com.romeao.bookstore.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    private static final Integer ID_ONE = 1;
    private static final String NAME_ONE = "Genre One";
    private static final Integer ID_TWO = 2;
    private static final String NAME_TWO = "Genre Two";

    private List<Genre> entityList;
    private Genre genreOne;
    private Genre genreTwo;

    @Mock
    private GenreRepository repository;

    @InjectMocks
    private GenreServiceImpl service;

    @BeforeEach
    void setUp() {
        genreOne = Genre.builder().id(ID_ONE).name(NAME_ONE).build();
        genreTwo = Genre.builder().id(ID_TWO).name(NAME_TWO).build();
        entityList = List.of(genreOne, genreTwo);
    }

    @Test
    void summarizeAll() {
        // given
        when(repository.findAll(any(Sort.class))).thenReturn(entityList);

        // when
        List<GenreSummaryDto> result = service.summarizeAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(NAME_ONE, result.get(0).getName());
        assertEquals(0, result.get(0).getLinks().size());
        assertEquals(NAME_TWO, result.get(1).getName());
        assertEquals(0, result.get(1).getLinks().size());

        verify(repository, times(1)).findAll(any(Sort.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void summarizeAll_withPaging() {
        //given
        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(genreOne)));
        int pageNum = 0;
        int limit = 1;

        // when
        Page<GenreSummaryDto> result = service.summarizeAll(pageNum, limit);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(NAME_ONE, result.getContent().get(0).getName());
    }
}