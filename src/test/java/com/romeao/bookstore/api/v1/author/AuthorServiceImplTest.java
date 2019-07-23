package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest extends AuthorTestUtils {

    private static final Integer ID_ONE = 1;
    private static final Integer ID_TWO = 2;
    private static final Integer ID_THREE = 3;
    private static final Integer NOT_FOUND_ID = 4;

    private static final String FIRST_ONE = "firstOne";
    private static final String FIRST_TWO = "firstTwo";
    private static final String FIRST_THREE = "firstThree";

    private static final String LAST_ONE = "lastOne";
    private static final String LAST_TWO = "lastTwo";
    private static final String LAST_THREE = "lastThree";

    private Author authorOne;
    private Author authorTwo;
    private List<Author> authorList;

    @Mock
    private AuthorRepository repository;

    @InjectMocks
    private AuthorServiceImpl service;

    @BeforeEach
    void setUp() {
        authorOne = Author.builder().id(ID_ONE).firstName(FIRST_ONE).lastName(LAST_ONE).build();
        authorTwo = Author.builder().id(ID_TWO).firstName(FIRST_TWO).lastName(LAST_TWO).build();
        Author authorThree = Author.builder()
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
        assertAuthorNameIsCorrect(dtoList.get(0), FIRST_ONE, LAST_ONE);
        assertHasSelfLink(dtoList.get(0), ID_ONE);
        assertAuthorNameIsCorrect(dtoList.get(1), FIRST_TWO, LAST_TWO);
        assertHasSelfLink(dtoList.get(1), ID_TWO);
        assertAuthorNameIsCorrect(dtoList.get(2), FIRST_THREE, LAST_THREE);
        assertHasSelfLink(dtoList.get(2), ID_THREE);

        verify(repository, times(1)).findAll(any(Sort.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll_withPaging() {
        // given
        final int PAGE_NUMBER = 1;
        final int PAGE_SIZE = 1;
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(authorTwo)));

        // when
        Page<AuthorDto> result = service.findAll(PAGE_NUMBER, PAGE_SIZE);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        AuthorDto dto = result.getContent().get(0);
        assertAuthorNameIsCorrect(dto, FIRST_TWO, LAST_TWO);
        assertHasSelfLink(dto, ID_TWO);

        verify(repository, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById() {
        // given
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(authorOne));

        // when
        AuthorDto dto = service.findById(ID_ONE);

        // then
        assertAuthorNameIsCorrect(dto, FIRST_ONE, LAST_ONE);
        assertHasSelfLink(dto, ID_ONE);
        verify(repository, times(1)).findById(ID_ONE);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_notFound() {
        // given
        when(repository.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());

        // when
        AuthorDto dto = service.findById(NOT_FOUND_ID);

        // then
        assertNull(dto);
        verify(repository, times(1)).findById(NOT_FOUND_ID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById() {
        // when
        service.deleteById(ID_ONE);

        // then
        verify(repository, times(1)).deleteById(ID_ONE);
        verifyNoMoreInteractions(repository);
    }
}