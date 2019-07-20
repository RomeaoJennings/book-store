package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.ErrorMessages;
import com.romeao.bookstore.errorhandling.ApiExceptionHandler;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {
    private static final Integer ID_FIRST = 1;
    private static final String NAME_FIRST = "Genre First";
    private static final String NAME_SECOND = "Genre Second";
    private static final String NAME_THIRD = "Genre Third";
    private static MockMvc mockMvc;
    private GenreDto GENRE_FIRST = new GenreDto();
    private GenreDto GENRE_SECOND = new GenreDto();
    private GenreDto GENRE_THIRD = new GenreDto();
    private List<GenreDto> dtoList;
    @Mock
    private GenreService service;
    @Mock
    private Page<GenreDto> page;
    @InjectMocks
    private GenreController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        GENRE_FIRST.setName(NAME_FIRST);
        GENRE_FIRST.getLinks().clear();
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

    @Test
    void getAllGenres_withBadPageAndLimit() throws Exception {
        mockMvc.perform(get("/api/v1/genres?limit=-1&pageNum=def"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo(ErrorMessages.INVALID_REQUEST_PARAMETERS)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.statusCode",
                        equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.subErrors", hasSize(2)))
                .andExpect(jsonPath("$.subErrors[*].field",
                        containsInAnyOrder("limit", "pageNum")))
                .andExpect(jsonPath("$.subErrors[*].rejectedValue",
                        containsInAnyOrder("-1", "def")))
                .andExpect(jsonPath("$.subErrors[*].message",
                        containsInAnyOrder(
                                ErrorMessages.BAD_LIMIT_VALUE,
                                ErrorMessages.INVALID_INTEGER)));

        verifyZeroInteractions(service);
    }

    @Test
    void getGenreById() throws Exception {
        // given
        GENRE_FIRST.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(ID_FIRST)));
        when(service.findById(ID_FIRST)).thenReturn(GENRE_FIRST);

        // when
        mockMvc.perform(get(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME_FIRST)))
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.links[0].name", equalTo(LinkNames.SELF)))
                .andExpect(jsonPath("$.links[0].url",
                        equalTo(Endpoints.Genre.byGenreId(ID_FIRST))));

        verify(service, times(1)).findById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteGenreById() throws Exception {
        // when
        mockMvc.perform(delete(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }
}