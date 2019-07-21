package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.ErrorMessages;
import com.romeao.bookstore.api.v1.util.TestUtils;
import com.romeao.bookstore.errorhandling.ApiExceptionHandler;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {
    private static final Integer ID_ONE = 1;
    private static final String FIRST_ONE = "First One";
    private static final String FIRST_TWO = "First Two";
    private static final String FIRST_THREE = "First Three";

    private static final String LAST_ONE = "Last One";
    private static final String LAST_TWO = "Last Two";
    private static final String LAST_THREE = "Last Three";

    private static final String MALFORMED_INT = "abc";
    private static final Integer NEGATIVE_PAGE_SIZE = -1;
    private static final String GENRE_ID_FIELD = "authorId";
    private static final String PAGE_SIZE_FIELD = "pageSize";

    private static MockMvc mockMvc;
    private AuthorDto authorOne;
    private AuthorDto authorTwo;
    private AuthorDto authorThree;
    private List<AuthorDto> dtoList;
    @Mock
    private AuthorService service;
    @Mock
    private Page<AuthorDto> page;
    @InjectMocks
    private AuthorController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        authorOne = new AuthorDto(FIRST_ONE, LAST_ONE);
        authorTwo = new AuthorDto(FIRST_TWO, LAST_TWO);
        authorThree = new AuthorDto(FIRST_THREE, LAST_THREE);

        dtoList = List.of(authorOne, authorTwo, authorThree);
    }

    @Test
    void getAllAuthors() throws Exception {
        // given
        when(service.findAll()).thenReturn(dtoList);

        mockMvc.perform(get(Endpoints.Author.URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors[*].firstName",
                        containsInAnyOrder(FIRST_ONE, FIRST_TWO, FIRST_THREE)))
                .andExpect(jsonPath("$.authors[*].lastName",
                        containsInAnyOrder(LAST_ONE, LAST_TWO, LAST_THREE)));

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllAuthors_withPageNumberAndPageSize() throws Exception {
        // given
        int pageNumber = 1;
        int previousPage = pageNumber - 1;
        int nextPage = pageNumber + 1;
        int pageSize = 1;
        int totalPages = 3;
        Long totalElements = 3L;

        when(page.getTotalElements()).thenReturn(totalElements);
        when(page.getContent()).thenReturn(List.of(authorTwo));
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.getSize()).thenReturn(pageSize);
        when(page.getNumber()).thenReturn(pageNumber);
        when(page.isFirst()).thenReturn(false);
        when(page.isLast()).thenReturn(false);
        when(service.findAll(pageNumber, pageSize)).thenReturn(page);

        // when
        mockMvc.perform(get(Endpoints.Author.byPageNumberAndPageSize(pageNumber, pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.totalElements",
                        equalTo(totalElements.intValue())))
                .andExpect(jsonPath("$.meta.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.meta.pageSize", equalTo(pageSize)))
                .andExpect(jsonPath("$.meta.pageNumber", equalTo(pageNumber)))
                .andExpect(jsonPath("$.meta.previousUrl",
                        equalTo(Endpoints.Author.byPageNumberAndPageSize(previousPage, pageSize))))
                .andExpect(jsonPath("$.meta.nextUrl",
                        equalTo(Endpoints.Author.byPageNumberAndPageSize(nextPage, pageSize))))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].firstName", equalTo(FIRST_TWO)))
                .andExpect(jsonPath("$.authors[0].lastName", equalTo(LAST_TWO)));

        verify(service, times(1)).findAll(pageNumber, pageSize);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllAuthors_withMalformedPageNumber() throws Exception {
        // when
        ResultActions request = mockMvc.perform(
                get(Endpoints.Author.URL + "?pageSize=1&pageNumber=" + MALFORMED_INT));

        // then
        TestUtils.validateMalformedIntJson(request, "pageNumber", MALFORMED_INT);
        verifyZeroInteractions(service);
    }

    @Test
    void getAllAuthors_withMalformedPageSize() throws Exception {
        // when
        ResultActions request = mockMvc.perform(
                get(Endpoints.Author.URL + "?pageNumber=1&pageSize=" + MALFORMED_INT));

        // then
        TestUtils.validateMalformedIntJson(request, PAGE_SIZE_FIELD, MALFORMED_INT);
        verifyZeroInteractions(service);
    }

    @Test
    void getAllAuthors_withNonPositivePageSize() throws Exception {
        mockMvc.perform(get(Endpoints.Author.byPageNumberAndPageSize(ID_ONE, NEGATIVE_PAGE_SIZE)))
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
    void getAuthorById() throws Exception {
        // given
        authorOne.getLinks().add(new Link(LinkNames.SELF, Endpoints.Author.byAuthorId(ID_ONE)));
        when(service.findById(ID_ONE)).thenReturn(authorOne);

        // when
        mockMvc.perform(get(Endpoints.Author.byAuthorId(ID_ONE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRST_ONE)))
                .andExpect(jsonPath("$.lastName", equalTo(LAST_ONE)))
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.links[0].name", equalTo(LinkNames.SELF)))
                .andExpect(jsonPath("$.links[0].url",
                        equalTo(Endpoints.Author.byAuthorId(ID_ONE))));

        verify(service, times(1)).findById(ID_ONE);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAuthorById_withMalformedId() throws Exception {
        ResultActions request = mockMvc.perform(
                get(Endpoints.Author.URL + "/" + MALFORMED_INT));
        TestUtils.validateMalformedIntJson(request, GENRE_ID_FIELD, MALFORMED_INT);
    }

    @Test
    void deleteAuthorById() throws Exception {
        // given
        when(service.findById(ID_ONE)).thenReturn(authorOne);

        // when
        mockMvc.perform(delete(Endpoints.Author.byAuthorId(ID_ONE)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteById(ID_ONE);
        verify(service, times(1)).findById(ID_ONE);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteAuthorById_withMalformedId() throws Exception {
        ResultActions request = mockMvc.perform(
                delete(Endpoints.Author.URL + "/" + MALFORMED_INT));
        TestUtils.validateMalformedIntJson(request, GENRE_ID_FIELD, MALFORMED_INT);
    }

    @Test
    void deleteAuthorById_withConstraintException() throws Exception {
        // given
        DataIntegrityViolationException expected = new DataIntegrityViolationException("Msg");
        doThrow(expected).when(service).deleteById(ID_ONE);
        when(service.findById(ID_ONE)).thenReturn(authorOne);

        // when
        mockMvc.perform(delete(Endpoints.Author.byAuthorId(ID_ONE)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(ErrorMessages.CANNOT_DELETE_RESOURCE)))
                .andExpect(jsonPath("$.debugMessage", equalTo(expected.toString())));
    }
}