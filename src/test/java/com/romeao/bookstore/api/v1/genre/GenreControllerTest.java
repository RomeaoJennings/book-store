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
import com.romeao.bookstore.util.ResourceMeta;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest extends TestUtils {
    private static final Integer ID_FIRST = 1;
    private static final Integer ID_SECOND = 2;
    private static final Integer ID_THIRD = 3;

    private static final String NAME_FIRST = "Genre First";
    private static final String NAME_SECOND = "Genre Second";
    private static final String NAME_THIRD = "Genre Third";

    private static final Integer NOT_FOUND_ID = 4;
    private static final String MALFORMED_INT = "abc";
    private static final Integer NEGATIVE_PAGE_SIZE = -1;

    private static final String GENRE_ID_FIELD = "genreId";

    private MockMvc mockMvc;

    private GenreDto GENRE_FIRST;
    private GenreDto GENRE_SECOND;
    private GenreDto GENRE_THIRD;
    private List<GenreDto> dtoList;

    @Mock
    private GenreService service;

    @InjectMocks
    private GenreController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        GENRE_FIRST = new GenreDto(NAME_FIRST);
        GENRE_FIRST.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(ID_FIRST)));
        GENRE_SECOND = new GenreDto(NAME_SECOND);
        GENRE_SECOND.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(ID_SECOND)));
        GENRE_THIRD = new GenreDto(NAME_THIRD);
        GENRE_THIRD.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(ID_THIRD)));

        dtoList = List.of(GENRE_FIRST, GENRE_SECOND, GENRE_THIRD);
    }

    @Test
    void getAllGenres() throws Exception {
        // GIVEN
        when(service.findAll()).thenReturn(dtoList);

        MvcResult result = mockMvc.perform(get(Endpoints.Genre.URL))
                .andExpect(status().isOk())
                .andReturn();

        GenreDtoList genreDtoList = convertJsonResponse(result, GenreDtoList.class);
        assertNotNull(genreDtoList);

        List<GenreDto> genres = genreDtoList.getGenres();
        assertNotNull(genres);
        assertEquals(dtoList.size(), genres.size());

        // assert that each Dto is in response.
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i), genres.get(i));
        }

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllGenres_withPageNumberAndPageSize() throws Exception {
        // GIVEN
        int pageNumber = 1;
        int pageSize = 1;
        long totalElements = 3L;

        Page<GenreDto> page = new PageImpl<>(List.of(GENRE_SECOND),
                PageRequest.of(pageNumber, pageSize, GenreService.DEFAULT_SORT), totalElements);

        ResourceMeta expectedMeta = ResourceMeta.builder()
                .fromPage(page, Endpoints.Genre::byPageNumberAndPageSize)
                .build();

        when(service.findAll(pageNumber, pageSize)).thenReturn(page);

        // WHEN
        MvcResult result = mockMvc.perform(get(Endpoints.Genre.byPageNumberAndPageSize(pageNumber
                , pageSize)))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        GenreDtoList list = convertJsonResponse(result, GenreDtoList.class);
        assertNotNull(list);
        assertEquals(expectedMeta, list.getMeta());
        assertEquals(1, list.getGenres().size());
        assertEquals(GENRE_SECOND, list.getGenres().get(0));

        verify(service, times(1)).findAll(pageNumber, pageSize);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllGenres_withMalformedPageNumber() throws Exception {
        // GIVEN
        String url = Endpoints.Genre.URL + "?" + PAGE_SIZE_FIELD + "=1&" + PAGE_NUMBER_FIELD +
                "=" + MALFORMED_INT;
        ApiError expectedError = ofMalformedIntParameter(PAGE_NUMBER_FIELD, MALFORMED_INT);

        // WHEN
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getAllGenres_withMalformedPageSize() throws Exception {
        // GIVEN
        String url = Endpoints.Genre.URL + "?" + PAGE_NUMBER_FIELD + "=1&" + PAGE_SIZE_FIELD +
                "=" + MALFORMED_INT;
        ApiError expectedError = ofMalformedIntParameter(PAGE_SIZE_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getAllGenres_withNonPositivePageSize() throws Exception {
        // GIVEN
        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST,
                ErrorMessages.INVALID_REQUEST_PARAMETERS);
        expectedError.getValidationErrors().add(new ApiValidationError(PAGE_SIZE_FIELD,
                ErrorMessages.PARAM_MUST_BE_POSITIVE, String.valueOf(NEGATIVE_PAGE_SIZE)));

        // WHEN / THEN
        mockMvc.perform(get(Endpoints.Genre.byPageNumberAndPageSize(ID_FIRST, NEGATIVE_PAGE_SIZE)))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getGenreById() throws Exception {
        // GIVEN
        when(service.findById(ID_FIRST)).thenReturn(GENRE_FIRST);

        // WHEN / THEN
        MvcResult result = mockMvc.perform(get(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isOk())
                .andReturn();

        GenreDto dto = convertJsonResponse(result, GenreDto.class);

        assertNotNull(dto);
        assertEquals(GENRE_FIRST, dto);

        verify(service, times(1)).findById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getGenreById_withMalformedId() throws Exception {
        // GIVEN
        ApiError expectedError = ofMalformedIntParameter(GENRE_ID_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(get(Endpoints.Genre.URL + "/" + MALFORMED_INT))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getGenreById_notFound() throws Exception {
        // GIVEN
        when(service.findById(NOT_FOUND_ID)).thenReturn(null);
        ApiError expectedError = new ApiError(HttpStatus.NOT_FOUND,
                ErrorMessages.RESOURCE_NOT_FOUND);

        mockMvc.perform(get(Endpoints.Genre.byGenreId(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(apiError(expectedError));

        verify(service, times(1)).findById(NOT_FOUND_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteGenreById() throws Exception {
        // GIVEN
        when(service.findById(ID_FIRST)).thenReturn(GENRE_FIRST);

        // WHEN
        mockMvc.perform(delete(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteById(ID_FIRST);
        verify(service, times(1)).findById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteByGenreId_notFound() throws Exception {
        // GIVEN
        when(service.findById(ID_FIRST)).thenReturn(null);
        ApiError expectedError = new ApiError(HttpStatus.NOT_FOUND,
                ErrorMessages.RESOURCE_NOT_FOUND);

        // WHEN
        mockMvc.perform(delete(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isNotFound())
                .andExpect(apiError(expectedError));

        verify(service, times(1)).findById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteGenreById_withMalformedId() throws Exception {
        // GIVEN
        ApiError expectedError = ofMalformedIntParameter(GENRE_ID_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(
                delete(Endpoints.Genre.URL + "/" + MALFORMED_INT)
        )
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void deleteGenreById_withConstraintException() throws Exception {
        // GIVEN
        DataIntegrityViolationException expected = new DataIntegrityViolationException("Msg");
        doThrow(expected).when(service).deleteById(ID_FIRST);
        when(service.findById(ID_FIRST)).thenReturn(GENRE_FIRST);

        ApiError expectedError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorMessages.CANNOT_DELETE_RESOURCE, expected.toString());

        // WHEN
        mockMvc.perform(delete(Endpoints.Genre.byGenreId(ID_FIRST)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(apiError(expectedError));
    }

    @Test
    void addGenre() throws Exception {
        // GIVEN
        when(service.save(any())).thenReturn(GENRE_FIRST);
        JSONObject request = new JSONObject();
        request.put(GenreDto.NAME_FIELD, NAME_FIRST);

        // WHEN
        MvcResult result = mockMvc.perform(post(Endpoints.Genre.URL)
                .content(request.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        GenreDto dto = convertJsonResponse(result, GenreDto.class);
        assertNotNull(dto);
        assertEquals(GENRE_FIRST, dto);

        verify(service, times(1)).save(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addGenre_alreadyExists() throws Exception {
        // GIVEN
        // set up mocked error from service
        ApiValidationError validationErr = new ApiValidationError(GenreDto.NAME_FIELD,
                ErrorMessages.RESOURCE_EXISTS, NAME_FIRST);
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorMessages.RESOURCE_EXISTS);
        apiError.getValidationErrors().add(validationErr);
        when(service.save(any())).thenThrow(new ApiException(apiError));

        // build JSON request
        JSONObject request = new JSONObject();
        request.put(GenreDto.NAME_FIELD, NAME_FIRST);

        // WHEN
        mockMvc.perform(post(Endpoints.Genre.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toString())
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(apiError(apiError));

        verify(service, times(1)).save(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addGenre_withInvalidRequest() throws Exception {
        // GIVEN
        JSONObject request = new JSONObject();
        final String INVALID_NAME = "genre";
        request.put(INVALID_NAME, "Action");
        ApiValidationError validationError = new ApiValidationError(GenreDto.NAME_FIELD,
                "must not be blank", null);
        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST,
                ErrorMessages.INVALID_REQUEST_PARAMETERS);
        expectedError.getValidationErrors().add(validationError);

        // WHEN / THEN
        mockMvc.perform(post(Endpoints.Genre.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toString())
        )
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }
}