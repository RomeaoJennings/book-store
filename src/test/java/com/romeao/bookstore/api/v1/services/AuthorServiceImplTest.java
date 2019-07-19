package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.AuthorDto;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.repositories.AuthorRepository;
import com.romeao.bookstore.util.LinkNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    private static final Integer ID_ONE = 1;
    private static final Integer ID_TWO = 2;
    private static final Integer ID_THREE = 3;

    private static final String FIRST_ONE = "firstOne";
    private static final String FIRST_TWO = "firstTwo";
    private static final String FIRST_THREE = "firstThree";

    private static final String LAST_ONE = "lastOne";
    private static final String LAST_TWO = "lastTwo";
    private static final String LAST_THREE = "lastThree";

    private Author authorOne;
    private Author authorTwo;
    private Author authorThree;
    private List<Author> authorList;

    @Mock
    private AuthorRepository repository;

    @InjectMocks
    private AuthorServiceImpl service;

    @BeforeEach
    void setUp() {
        authorOne = Author.builder().id(ID_ONE).firstName(FIRST_ONE).lastName(LAST_ONE).build();
        authorTwo = Author.builder().id(ID_TWO).firstName(FIRST_TWO).lastName(LAST_TWO).build();
        authorThree = Author.builder()
                .id(ID_THREE)
                .firstName(FIRST_THREE)
                .lastName(LAST_THREE)
                .build();
        authorList = List.of(authorOne, authorTwo, authorThree);
    }

    @Test
    void findAll() {
        // given
        when(repository.findAll(any(Sort.class))).thenReturn(authorList);

        // when
        List<AuthorDto> dtoList = service.findAll();

        // then
        assertNotNull(dtoList);
        assertEquals(3, dtoList.size());
        assertEquals(FIRST_ONE, dtoList.get(0).getFirstName());
        assertEquals(LAST_ONE, dtoList.get(0).getLastName());
        assertEquals(FIRST_TWO, dtoList.get(1).getFirstName());
        assertEquals(LAST_TWO, dtoList.get(1).getLastName());
        assertEquals(FIRST_THREE, dtoList.get(2).getFirstName());
        assertEquals(LAST_THREE, dtoList.get(2).getLastName());

        assertEquals(Endpoints.Author.byAuthorId(ID_ONE),
                dtoList.get(0).getLinks().get(0).getUrl());
        assertEquals(LinkNames.SELF, dtoList.get(0).getLinks().get(0).getName());
    }
}