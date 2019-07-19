package com.romeao.bookstore.api.v1.controllers;

import com.romeao.bookstore.api.v1.models.GenreDto;
import com.romeao.bookstore.api.v1.services.GenreService;
import com.romeao.bookstore.api.v1.util.Endpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    private static final String NAME_FIRST = "Genre First";
    private static final String NAME_SECOND = "Genre Second";
    private static final String NAME_THIRD = "Genre Third";

    private static final GenreDto GENRE_FIRST = new GenreDto();
    private static final GenreDto GENRE_SECOND = new GenreDto();
    private static final GenreDto GENRE_THIRD = new GenreDto();

    private List<GenreDto> dtoList;

    @Mock
    private GenreService service;

    @Mock
    private Page<GenreDto> page;

    @InjectMocks
    private GenreController controller;

    private static MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        GENRE_FIRST.setName(NAME_FIRST);
        GENRE_SECOND.setName(NAME_SECOND);
        GENRE_THIRD.setName(NAME_THIRD);

        dtoList = List.of(GENRE_FIRST, GENRE_SECOND, GENRE_THIRD);
    }

    @Test
    void allMocksLoad() {
        assertNotNull(page);
        assertNotNull(service);
    }

    @Test
    void getAllGenres() throws Exception {
        // given
        when(service.findAll()).thenReturn(dtoList);

        mockMvc.perform(get(Endpoints.Genre.URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres[*].name",
                        containsInAnyOrder(NAME_FIRST, NAME_SECOND, NAME_THIRD)));

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllGenres_withPageAndLimit() throws Exception {
        // given
        int pageNum = 1;
        int pageLimit = 1;
        Long count = 3L;
        when(page.getTotalElements()).thenReturn(count);
        when(page.getContent()).thenReturn(List.of(GENRE_SECOND));
        when(page.isFirst()).thenReturn(false);
        when(page.isLast()).thenReturn(false);
        when(service.findAll(pageNum, pageLimit)).thenReturn(page);

        // when
        mockMvc.perform(get(Endpoints.Genre.byPageAndLimit(pageNum, pageLimit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.count", equalTo(count.intValue())))
                .andExpect(jsonPath("$.meta.limit", equalTo(pageLimit)))
                .andExpect(jsonPath("$.meta.pageNum", equalTo(pageNum)))
                .andExpect(jsonPath("$.meta.previousUrl",
                        equalTo(Endpoints.Genre.byPageAndLimit(pageNum - 1, pageLimit))))
                .andExpect(jsonPath("$.meta.nextUrl",
                        equalTo(Endpoints.Genre.byPageAndLimit(pageNum + 1, pageLimit))))
                .andExpect(jsonPath("$.genres", hasSize(1)))
                .andExpect(jsonPath("$.genres[0].name", equalTo(NAME_SECOND)));

        verify(service, times(1)).findAll(pageNum, pageLimit);
        verifyNoMoreInteractions(service);
    }
}