package com.romeao.bookstore.api.v1.controllers;

import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import com.romeao.bookstore.api.v1.services.GenreService;
import com.romeao.bookstore.api.v1.util.Endpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    private static final String NAME_ONE = "Genre One";
    private static final String NAME_TWO = "Genre Two";

    private static List<GenreSummaryDto> dtoList;
    @Mock
    private static GenreService service;

    @InjectMocks
    private static GenreController controller;

    private static MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        dtoList = new ArrayList<>();
        GenreSummaryDto dto1 = new GenreSummaryDto();
        dto1.setName(NAME_ONE);
        dtoList.add(dto1);
        GenreSummaryDto dto2 = new GenreSummaryDto();
        dto2.setName(NAME_TWO);
        dtoList.add(dto2);
    }

    @Test
    void getAllGenres() throws Exception {
        // given
        when(service.summarizeAll()).thenReturn(dtoList);

        mockMvc.perform(get(Endpoints.Genre.URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres[*].name",
                        containsInAnyOrder(NAME_ONE, NAME_TWO)));

        verify(service, times(1)).summarizeAll();
        verifyNoMoreInteractions(service);
    }
}