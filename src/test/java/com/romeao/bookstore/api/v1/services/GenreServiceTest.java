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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    private static final Integer ID_ONE = 1;
    private static final String NAME_ONE = "Genre One";
    private static final Integer ID_TWO = 2;
    private static final String NAME_TWO = "Genre Two";

    private static List<Genre> entityList;
    @Mock
    private static GenreRepository repository;
    @InjectMocks
    private static GenreService service;

    @BeforeEach
    void setUp() {
        entityList = new ArrayList<>();
        entityList.add(Genre.builder().id(ID_ONE).name(NAME_ONE).build());
        entityList.add(Genre.builder().id(ID_TWO).name(NAME_TWO).build());
    }

    @Test
    void summarizeAll() {
        // given
        when(repository.findAll()).thenReturn(entityList);

        // when
        List<GenreSummaryDto> result = service.summarizeAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(NAME_ONE, result.get(0).getName());
        assertEquals(0, result.get(0).getLinks().size());
        assertEquals(NAME_TWO, result.get(1).getName());
        assertEquals(0, result.get(1).getLinks().size());

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
}