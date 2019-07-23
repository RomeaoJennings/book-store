package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.ErrorMessages;
import com.romeao.bookstore.api.v1.util.TestUtils;
import com.romeao.bookstore.errorhandling.ApiError;
import com.romeao.bookstore.errorhandling.ApiExceptionHandler;
import com.romeao.bookstore.errorhandling.ApiValidationError;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import com.romeao.bookstore.util.ResourceMeta;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest extends TestUtils {
    private static final String FIRST_ONE = "First One";
    private static final String FIRST_TWO = "First Two";
    private static final String FIRST_THREE = "First Three";

    private static final String LAST_ONE = "Last One";
    private static final String LAST_TWO = "Last Two";
    private static final String LAST_THREE = "Last Three";

    private static final String AUTHOR_ID_FIELD = "authorId";
    private static final Integer ID_FIRST = 1;
    private static final Integer ID_SECOND = 2;

    private static final String MALFORMED_INT = "abc";
    private static final Integer NEGATIVE_PAGE_SIZE = -1;
    private static final Integer ID_THIRD = 3;
    private static final Integer NOT_FOUND_ID = 4;

    private static MockMvc mockMvc;
    private AuthorDto authorOne;
    private AuthorDto authorTwo;
    private List<AuthorDto> dtoList;

    @Mock
    private AuthorService service;

    @InjectMocks
    private AuthorController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        authorOne = new AuthorDto(FIRST_ONE, LAST_ONE);
        authorOne.getLinks().add(new Link(LinkNames.SELF, Endpoints.Author.byAuthorId(ID_FIRST)));

        authorTwo = new AuthorDto(FIRST_TWO, LAST_TWO);
        authorTwo.getLinks().add(new Link(LinkNames.SELF, Endpoints.Author.byAuthorId(ID_SECOND)));

        AuthorDto authorThree = new AuthorDto(FIRST_THREE, LAST_THREE);
        authorThree.getLinks().add(new Link(LinkNames.SELF, Endpoints.Author.byAuthorId(ID_THIRD)));

        dtoList = List.of(authorOne, authorTwo, authorThree);
    }

    @Test
    void getAllAuthors() throws Exception {
        // GIVEN
        when(service.findAll()).thenReturn(dtoList);

        MvcResult result = mockMvc.perform(get(Endpoints.Author.URL))
                .andExpect(status().isOk())
                .andReturn();

        // convert Json response body back to list of authors
        AuthorDtoList authorDtoList = convertJsonResponse(result, AuthorDtoList.class);
        assertNotNull(authorDtoList);

        List<AuthorDto> authors = authorDtoList.getAuthors();
        assertNotNull(authors);
        assertEquals(dtoList.size(), authors.size());

        // assert that each Dto is in response.
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i), authors.get(i));
        }

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllAuthors_withPageNumberAndPageSize() throws Exception {
        // GIVEN
        int pageNumber = 1;
        int pageSize = 1;
        long totalElements = 3L;

        Page<AuthorDto> page = new PageImpl<>(List.of(authorTwo),
                PageRequest.of(pageNumber, pageSize, AuthorService.DEFAULT_SORT), totalElements);

        ResourceMeta expectedMeta = ResourceMeta.builder()
                .fromPage(page, Endpoints.Author::byPageNumberAndPageSize)
                .build();

        when(service.findAll(pageNumber, pageSize)).thenReturn(page);

        // WHEN
        MvcResult result = mockMvc.perform(
                get(Endpoints.Author.byPageNumberAndPageSize(pageNumber, pageSize))
        )
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        AuthorDtoList list = convertJsonResponse(result, AuthorDtoList.class);
        assertNotNull(list);
        assertEquals(expectedMeta, list.getMeta());
        assertEquals(1, list.getAuthors().size());
        assertEquals(authorTwo, list.getAuthors().get(0));

        verify(service, times(1)).findAll(pageNumber, pageSize);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllAuthors_withMalformedPageNumber() throws Exception {
        // GIVEN
        String url =
                Endpoints.Author.URL + "?pageSize=1&" + PAGE_NUMBER_FIELD + "=" + MALFORMED_INT;
        ApiError expectedError = ofMalformedIntParameter(PAGE_NUMBER_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getAllAuthors_withMalformedPageSize() throws Exception {
        // GIVEN
        String url = Endpoints.Author.URL + "?" + PAGE_NUMBER_FIELD + "=1&" + PAGE_SIZE_FIELD +
                "=" + MALFORMED_INT;
        ApiError expectedError = ofMalformedIntParameter(PAGE_SIZE_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getAllAuthors_withNonPositivePageSize() throws Exception {
        // GIVEN
        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST,
                ErrorMessages.INVALID_REQUEST_PARAMETERS);
        expectedError.getValidationErrors().add(new ApiValidationError(PAGE_SIZE_FIELD,
                ErrorMessages.PARAM_MUST_BE_POSITIVE, String.valueOf(NEGATIVE_PAGE_SIZE)));

        // WHEN / THEN
        mockMvc.perform(get(Endpoints.Author.byPageNumberAndPageSize(ID_FIRST, NEGATIVE_PAGE_SIZE)))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getAuthorById() throws Exception {
        // GIVEN
        when(service.findById(ID_FIRST)).thenReturn(authorOne);

        // WHEN / THEN
        MvcResult result = mockMvc.perform(get(Endpoints.Author.byAuthorId(ID_FIRST)))
                .andExpect(status().isOk())
                .andReturn();

        AuthorDto dto = convertJsonResponse(result, AuthorDto.class);

        assertNotNull(dto);
        assertEquals(authorOne, dto);

        verify(service, times(1)).findById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAuthorById_withMalformedId() throws Exception {
        // GIVEN
        ApiError expectedError = ofMalformedIntParameter(AUTHOR_ID_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(get(Endpoints.Author.URL + "/" + MALFORMED_INT))
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void getAuthorById_notFound() throws Exception {
        // GIVEN
        when(service.findById(NOT_FOUND_ID)).thenReturn(null);
        ApiError expectedError = new ApiError(HttpStatus.NOT_FOUND,
                ErrorMessages.RESOURCE_NOT_FOUND);

        mockMvc.perform(get(Endpoints.Author.byAuthorId(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(apiError(expectedError));

        verify(service, times(1)).findById(NOT_FOUND_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteAuthorById() throws Exception {
        // GIVEN
        when(service.findById(ID_FIRST)).thenReturn(authorOne);

        // WHEN
        mockMvc.perform(delete(Endpoints.Author.byAuthorId(ID_FIRST)))
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
        mockMvc.perform(delete(Endpoints.Author.byAuthorId(ID_FIRST)))
                .andExpect(status().isNotFound())
                .andExpect(apiError(expectedError));

        verify(service, times(1)).findById(ID_FIRST);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteAuthorById_withMalformedId() throws Exception {
        // GIVEN
        ApiError expectedError = ofMalformedIntParameter(AUTHOR_ID_FIELD, MALFORMED_INT);

        // WHEN / THEN
        mockMvc.perform(
                delete(Endpoints.Author.URL + "/" + MALFORMED_INT)
        )
                .andExpect(status().isBadRequest())
                .andExpect(apiError(expectedError));

        verifyZeroInteractions(service);
    }

    @Test
    void deleteAuthorById_withConstraintException() throws Exception {
        // GIVEN
        DataIntegrityViolationException expected = new DataIntegrityViolationException("Msg");
        doThrow(expected).when(service).deleteById(ID_FIRST);
        when(service.findById(ID_FIRST)).thenReturn(authorOne);

        ApiError expectedError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorMessages.CANNOT_DELETE_RESOURCE, expected.toString());

        // WHEN
        mockMvc.perform(delete(Endpoints.Author.byAuthorId(ID_FIRST)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(apiError(expectedError));
    }
}