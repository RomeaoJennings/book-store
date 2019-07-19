package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.GenreDto;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.domain.Genre;
import com.romeao.bookstore.repositories.GenreRepository;
import com.romeao.bookstore.util.LinkNames;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    private static final Integer ID_ONE = 1;
    private static final String NAME_ONE = "Genre One";
    private static final Integer ID_TWO = 2;
    private static final String NAME_TWO = "Genre Two";

    private static final Integer NOT_FOUND_ID = 3;

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
    void testFindAll() {
        // given
        when(repository.findAll(any(Sort.class))).thenReturn(entityList);

        // when
        List<GenreDto> result = service.findAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(NAME_ONE, result.get(0).getName());
        assertEquals(1, result.get(0).getLinks().size());
        assertEquals(LinkNames.SELF, result.get(0).getLinks().get(0).getName());
        assertEquals(Endpoints.Genre.byGenreId(ID_ONE), result.get(0).getLinks().get(0).getUrl());
        assertEquals(NAME_TWO, result.get(1).getName());
        assertEquals(1, result.get(1).getLinks().size());
        assertEquals(LinkNames.SELF, result.get(1).getLinks().get(0).getName());
        assertEquals(Endpoints.Genre.byGenreId(ID_TWO), result.get(1).getLinks().get(0).getUrl());

        verify(repository, times(1)).findAll(any(Sort.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindAll_withPaging() {
        // given
        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(genreOne)));
        int pageNum = 0;
        int limit = 1;

        // when
        Page<GenreDto> result = service.findAll(pageNum, limit);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(NAME_ONE, result.getContent().get(0).getName());
        assertEquals(1, result.getContent().get(0).getLinks().size());
        assertEquals(LinkNames.SELF, result.getContent().get(0).getLinks().get(0).getName());
        assertEquals(Endpoints.Genre.byGenreId(ID_ONE),
                result.getContent().get(0).getLinks().get(0).getUrl());
    }

    @Test
    void testFindById() {
        // given
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(genreOne));

        // when
        GenreDto dto = service.findById(ID_ONE);

        // then
        assertNotNull(dto);
        assertEquals(NAME_ONE, dto.getName());
        assertEquals(1, dto.getLinks().size());
        assertEquals(LinkNames.SELF, dto.getLinks().get(0).getName());
        assertEquals(Endpoints.Genre.byGenreId(ID_ONE), dto.getLinks().get(0).getUrl());
    }

    @Test
    void testFindById_notFound() {
        // given
        when(repository.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());

        // when
        GenreDto dto = service.findById(NOT_FOUND_ID);

        // then
        assertNull(dto);
        verify(repository, times(1)).findById(NOT_FOUND_ID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testDeleteById() {
        // when
        service.deleteById(ID_ONE);

        // then
        verify(repository, times(1)).deleteById(ID_ONE);
        verifyNoMoreInteractions(repository);
    }
}