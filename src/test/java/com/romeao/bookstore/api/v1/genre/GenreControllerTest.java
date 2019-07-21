package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.ErrorMessages;
import com.romeao.bookstore.api.v1.util.TestUtils;
import com.romeao.bookstore.errorhandling.ApiError;
import com.romeao.bookstore.errorhandling.ApiException;
import com.romeao.bookstore.errorhandling.ApiExceptionHandler;
import com.romeao.bookstore.errorhandling.ApiValidationError;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {
    private static final Integer ID_FIRST = 1;
    private static final String NAME_FIRST = "Genre First";
    private static final String NAME_SECOND = "Genre Second";
    private static final String NAME_THIRD = "Genre Third";
    private static final String MALFORMED_INT = "abc";
    private static final Integer NEGATIVE_PAGE_SIZE = -1;
    private static final String GENRE_ID_FIELD = "genreId";
    private static final String PAGE_SIZE_FIELD = "pageSize";
    private static final Integer NOT_FOUND_ID = 4;
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
    void getAllGenres_withPageNumberAndPageSize() throws Exception {
        // given
        int pageNumber = 1;
        int previousPage = pageNumber - 1;
        int nextPage = pageNumber + 1;
        int pageSize = 1;
        int totalPages = 3;
        Long totalElements = 3L;

        when(page.getTotalElements()).thenReturn(totalElements);
        when(page.getContent()).thenReturn(List.of(GENRE_SECOND));
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.getSize()).thenReturn(pageSize);
        when(page.getNumber()).thenReturn(pageNumber);
        when(page.isFirst()).thenReturn(false);
        when(page.isLast()).thenReturn(false);
        when(service.findAll(pageNumber, pageSize)).thenReturn(page);

        // when
        mockMvc.perform(get(Endpoints.Genre.byPageNumberAndPageSize(pageNumber, pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.totalElements",
                        equalTo(totalElements.intValue())))
                .andExpect(jsonPath("$.meta.pageSize", equalTo(pageSize)))
                .andExpect(jsonPath("$.meta.pageNumber", equalTo(pageNumber)))
                .andExpect(jsonPath("$.meta.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.meta.previousUrl",
                        equalTo(Endpoints.Genre.byPageNumberAndPageSize(previousPage, pageSize))))
                .andExpect(jsonPath("$.meta.nextUrl",
                        equalTo(Endpoints.Genre.byPageNumberAndPageSize(nextPage, pageSize))))
                .andExpect(jsonPath("$.genres", hasSize(1)))
                .andExpect(jsonPath("$.genres[0].name", equalTo(NAME_SECOND)));

        verify(service, times(1)).findAll(pageNumber, pageSize);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllGenres_withMalformedPageNumber() throws Exception {
        // when
        ResultActions request = mockMvc.perform(
                get(Endpoints.Genre.URL + "?pageSize=1&pageNumber=" + MALFORMED_INT));

        // then
        TestUtils.validateMalformedIntJson(request, "pageNumber", MALFORMED_INT);
        verifyZeroInteractions(service);
    }

    @Test
    void getAllGenres_withMalformedPageSize() throws Exception {
        // when
        ResultActions request = mockMvc.perform(
                get(Endpoints.Genre.URL + "?pageNumber=1&pageSize=" + MALFORMED_INT));

        // then
        TestUtils.validateMalformedIntJson(request, PAGE_SIZE_FIELD, MALFORMED_INT);
        verifyZeroInteractions(service);
    }

    @Test
    void getAllGenres_withNonPositivePageSize() throws Exception {
        mockMvc.perform(get(Endpoints.Genre.byPageNumberAndPageSize(ID_FIRST, NEGATIVE_PAGE_SIZE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo(ErrorMessages.INVALID_REQUEST_PARAMETERS)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.statusCode",
                        equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.subErrors", hasSize(1)))
                .andExpect(jsonPath("$.subErrors[0].field", equalTo(PAGE_SIZE_FIELD)))
                .andExpect(jsonPath("$.subErrors[0].rejectedValue",
                        equalTo(String.valueOf(NEGATIVE_PAGE_SIZE))))
                .andExpect(jsonPath("$.subErrors[0].message",
                        equalTo(ErrorMessages.PARAM_MUST_BE_POSITIVE)));

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
    void getGenreById_withMalformedId() throws Exception {
        ResultActions request = mockMvc.perform(
                get(Endpoints.Genre.URL + "/" + MALFORMED_INT));
        TestUtils.validateMalformedIntJson(request, GENRE_ID_FIELD, MALFORMED_INT);
    }

    @Test
    void getGenreById_notFound() throws Exception {
        // given
        when(service.findById(NOT_FOUND_ID)).thenReturn(null);

        mockMvc.perform(get(Endpoints.Genre.byGenreId(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(ErrorMessages.RESOURCE_NOT_FOUND)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.name())));
    }

    @Test
    void deleteGenreById() throws Exception {
        // when
        mockMvc.perform(delete(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteGenreById_withMalformedId() throws Exception {
        ResultActions request = mockMvc.perform(
                delete(Endpoints.Genre.URL + "/" + MALFORMED_INT));
        TestUtils.validateMalformedIntJson(request, GENRE_ID_FIELD, MALFORMED_INT);
        verifyZeroInteractions(service);
    }

    @Test
    void deleteGenreById_withConstraintException() throws Exception {
        // given
        DataIntegrityViolationException expected = new DataIntegrityViolationException("Msg");
        doThrow(expected).when(service).deleteById(ID_FIRST);

        // when
        mockMvc.perform(delete(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(ErrorMessages.CANNOT_DELETE_RESOURCE)))
                .andExpect(jsonPath("$.debugMessage", equalTo(expected.toString())));
        verify(service, times(1)).deleteById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void addGenre() throws Exception {
        // given
        when(service.save(any())).thenReturn(GENRE_FIRST);
        JSONObject request = new JSONObject();
        request.put("name", NAME_FIRST);

        // when
        mockMvc.perform(post(Endpoints.Genre.URL)
                .content(request.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME_FIRST)));

        verify(service, times(1)).save(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addGenre_alreadyExists() throws Exception {
        // given

        // set up mocked error from service
        ApiValidationError validationErr = new ApiValidationError("name",
                ErrorMessages.RESOURCE_EXISTS, NAME_FIRST);
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorMessages.RESOURCE_EXISTS);
        apiError.getSubErrors().add(validationErr);
        when(service.save(any())).thenThrow(new ApiException(apiError));

        // build JSON request
        JSONObject request = new JSONObject();
        request.put("name", NAME_FIRST);

        // when
        mockMvc.perform(post(Endpoints.Genre.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toString())
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message",
                        equalTo(ErrorMessages.RESOURCE_EXISTS)))
                .andExpect(jsonPath("$.status",
                        equalTo(HttpStatus.UNPROCESSABLE_ENTITY.name())))
                .andExpect(jsonPath("$.subErrors[0].rejectedValue",
                        equalTo(NAME_FIRST)))
                .andExpect(jsonPath("$.subErrors[0].field", equalTo("name")));
        verify(service, times(1)).save(any());
        verifyNoMoreInteractions(service);
    }
}