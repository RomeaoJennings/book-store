package com.romeao.bookstore.api.v1.controllers;

import com.romeao.bookstore.api.v1.models.AuthorDto;
import com.romeao.bookstore.api.v1.services.AuthorService;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
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
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

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
    void getAllAuthors_withPageAndLimit() throws Exception {
        // given
        int pageNum = 1;
        int pageLimit = 1;
        Long count = 3L;
        when(page.getTotalElements()).thenReturn(count);
        when(page.getContent()).thenReturn(List.of(authorTwo));
        when(page.isFirst()).thenReturn(false);
        when(page.isLast()).thenReturn(false);
        when(service.findAll(pageNum, pageLimit)).thenReturn(page);

        // when
        mockMvc.perform(get(Endpoints.Author.byPageAndLimit(pageNum, pageLimit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.count", equalTo(count.intValue())))
                .andExpect(jsonPath("$.meta.limit", equalTo(pageLimit)))
                .andExpect(jsonPath("$.meta.pageNum", equalTo(pageNum)))
                .andExpect(jsonPath("$.meta.previousUrl",
                        equalTo(Endpoints.Author.byPageAndLimit(pageNum - 1, pageLimit))))
                .andExpect(jsonPath("$.meta.nextUrl",
                        equalTo(Endpoints.Author.byPageAndLimit(pageNum + 1, pageLimit))))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].firstName", equalTo(FIRST_TWO)))
                .andExpect(jsonPath("$.authors[0].lastName", equalTo(LAST_TWO)));

        verify(service, times(1)).findAll(pageNum, pageLimit);
        verifyNoMoreInteractions(service);
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
    void deleteAuthorById() throws Exception {
        // when
        mockMvc.perform(delete(Endpoints.Author.byAuthorId(ID_ONE)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteById(ID_ONE);
        verifyNoMoreInteractions(service);
    }
}