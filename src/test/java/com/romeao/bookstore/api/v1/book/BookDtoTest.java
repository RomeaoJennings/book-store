package com.romeao.bookstore.api.v1.book;

import com.romeao.bookstore.api.v1.author.AuthorDto;
import com.romeao.bookstore.api.v1.genre.GenreDto;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {
    private static final int GENRE_ID_ONE = 1;
    private static final int GENRE_ID_TWO = 2;
    private static final String GENRE_NAME_ONE = "gn1";
    private static final String GENRE_NAME_TWO = "gn2";

    private static final int AUTHOR_ID_ONE = 3;
    private static final int AUTHOR_ID_TWO = 4;
    private static final String AUTHOR_FIRST_ONE = "af1";
    private static final String AUTHOR_FIRST_TWO = "af2";
    private static final String AUTHOR_LAST_ONE = "al1";
    private static final String AUTHOR_LAST_TWO = "al2";

    private static final int ID = 0;
    private static final String NAME = "bookName";
    private static final double PRICE_DBL = 10.99;
    private static final BigDecimal PRICE = BigDecimal.valueOf(PRICE_DBL);
    private static final String PUBLISHER = "publisher";

    private GenreDto genreOne;
    private GenreDto genreTwo;
    private AuthorDto authorOne;
    private AuthorDto authorTwo;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        // Set up genres
        genreOne = new GenreDto(GENRE_NAME_ONE);
        genreOne.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(GENRE_ID_ONE)));
        genreTwo = new GenreDto(GENRE_NAME_TWO);
        genreTwo.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(GENRE_ID_TWO)));

        // Set up authors
        authorOne = new AuthorDto(AUTHOR_FIRST_ONE, AUTHOR_LAST_ONE);
        authorOne.getLinks().add(new Link(LinkNames.SELF,
                Endpoints.Author.byAuthorId(AUTHOR_ID_ONE)));
        authorTwo = new AuthorDto(AUTHOR_FIRST_TWO, AUTHOR_LAST_TWO);
        authorTwo.getLinks().add(new Link(LinkNames.SELF,
                Endpoints.Author.byAuthorId(AUTHOR_ID_TWO)));

        // Set up Test Book
        bookDto = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
        bookDto.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));
    }

    @Test
    void of() {
        assertNotNull(bookDto);
        assertEquals(NAME, bookDto.getName());
        assertEquals(PUBLISHER, bookDto.getPublisher());
        assertEquals(PRICE, bookDto.getPrice());
        assertEquals(2, bookDto.getAuthors().size());
        assertTrue(bookDto.getAuthors().containsAll(List.of(authorOne, authorTwo)));
        assertEquals(2, bookDto.getGenres().size());
        assertTrue(bookDto.getGenres().containsAll(List.of(genreOne, genreTwo)));
    }


    @Test
    void setName() {
        // GIVEN
        String newName = "newName";

        // WHEN
        bookDto.setName(newName);

        // THEN
        assertEquals(bookDto.getName(), newName);
    }

    @Test
    void setPublisher() {
        // GIVEN
        String newPublisher = "newPublisher";

        // WHEN
        bookDto.setPublisher(newPublisher);

        // THEN
        assertEquals(bookDto.getPublisher(), newPublisher);
    }

    @Test
    void setPrice() {
        // GIVEN
        BigDecimal newPrice = BigDecimal.valueOf(1.23);

        // WHEN
        bookDto.setPrice(newPrice);

        // THEN
        assertEquals(bookDto.getPrice(), newPrice);
    }

    @Test
    void setPrice_withDouble() {
        // GIVEN
        double newPrice = 1.23;

        // WHEN
        bookDto.setPrice(newPrice);

        // THEN
        assertEquals(bookDto.getPrice(), BigDecimal.valueOf(newPrice));
    }

    @Test
    void equals() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorTwo, authorOne),
                List.of(genreTwo, genreOne));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));

        // THEN
        assertEquals(bookDto, dto2);
    }

    @Test
    void equals_withSameObject() {
        assertEquals(bookDto, bookDto);
    }

    @Test
    void equals_withNull() {
        assertNotEquals(bookDto, null);
    }

    @Test
    void equals_withDifferentClass() {
        assertNotEquals(bookDto, 1);
    }

    @Test
    void equals_withDifferentName() {
        // GIVEN
        String newName = "newName";
        BookDto dto2 = BookDto.of(newName, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));


        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentPublisher() {
        // GIVEN
        String newPublisher = "newName";
        BookDto dto2 = BookDto.of(NAME, newPublisher, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));


        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentPrice() {
        // GIVEN
        double newPrice = 1.11;
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, newPrice, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));

        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentAuthorListSize() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));


        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentAuthorList() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(new AuthorDto("test", "test"
                ), authorTwo),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));


        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentGenreListSize() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));


        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentGenreList() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, new GenreDto("name")));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));


        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentLinkSize() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));

        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void equals_withDifferentLinks() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link("differentName", "differentUrl"));

        // THEN
        assertNotEquals(bookDto, dto2);
    }

    @Test
    void testHashCode() {
        // GIVEN
        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorOne, authorTwo),
                List.of(genreOne, genreTwo));
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));

        // THEN
        assertEquals(bookDto.hashCode(), dto2.hashCode());
    }

    @Test
    void testHashCode_withDifferentOrderedCollections() {
        // GIVEN
        Link newLink = new Link("name", "testUrl");
        bookDto.getLinks().add(newLink);

        BookDto dto2 = BookDto.of(NAME, PUBLISHER, PRICE_DBL, List.of(authorTwo, authorOne),
                List.of(genreTwo, genreOne));
        dto2.getLinks().add(newLink);
        dto2.getLinks().add(new Link(LinkNames.SELF, Endpoints.Book.byBookId(ID)));

        // THEN
        assertEquals(bookDto.hashCode(), dto2.hashCode());
    }
}