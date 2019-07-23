package com.romeao.bookstore.api.v1.book;

import com.romeao.bookstore.api.v1.author.AuthorDto;
import com.romeao.bookstore.api.v1.genre.GenreDto;
import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.domain.Book;
import com.romeao.bookstore.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {
    private static final String GENRE_NAME_ONE = "gn1";
    private static final String GENRE_NAME_TWO = "gn2";

    private static final String AUTHOR_FIRST_ONE = "af1";
    private static final String AUTHOR_FIRST_TWO = "af2";
    private static final String AUTHOR_LAST_ONE = "al1";
    private static final String AUTHOR_LAST_TWO = "al2";

    private static final String NAME = "bookName";
    private static final double PRICE_DBL = 10.99;
    private static final BigDecimal PRICE = BigDecimal.valueOf(PRICE_DBL);
    private static final String PUBLISHER = "publisher";

    private static final BookMapper mapper = BookMapper.INSTANCE;

    private Book entity;
    private BookDto dto;

    private void setUpEntity() {
        Author authorOne = Author.builder()
                .firstName(AUTHOR_FIRST_ONE)
                .lastName(AUTHOR_LAST_ONE)
                .build();
        Author authorTwo = Author.builder()
                .firstName(AUTHOR_FIRST_TWO)
                .lastName(AUTHOR_LAST_TWO)
                .build();

        Genre genreOne = Genre.builder()
                .name(GENRE_NAME_ONE)
                .build();
        Genre genreTwo = Genre.builder()
                .name(GENRE_NAME_TWO)
                .build();

        entity = Book.builder()
                .name(NAME)
                .price(PRICE)
                .publisher(PUBLISHER)
                .authors(authorOne, authorTwo)
                .genres(genreOne, genreTwo)
                .build();
    }

    private void setUpDto() {
        AuthorDto authorOne = new AuthorDto(AUTHOR_FIRST_ONE, AUTHOR_LAST_ONE);
        AuthorDto authorTwo = new AuthorDto(AUTHOR_FIRST_TWO, AUTHOR_LAST_TWO);

        GenreDto genreOne = new GenreDto(GENRE_NAME_ONE);
        GenreDto genreTwo = new GenreDto(GENRE_NAME_TWO);

        dto = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
    }

    @BeforeEach
    void setUp() {
        setUpDto();
        setUpEntity();
    }

    @Test
    void toEntity_withNullDto() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_withNullEntity() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toDto() {
        // WHEN
        BookDto bookDto = mapper.toDto(entity);

        // THEN
        assertNotNull(bookDto);
        assertEquals(dto, bookDto);

    }

    @Test
    void toEntity() {
        // WHEN
        Book book = mapper.toEntity(dto);

        // THEN
        assertNotNull(book);
        assertEquals(entity, book);
    }
}