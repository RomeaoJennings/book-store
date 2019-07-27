package com.romeao.bookstore.api.v1.book;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.api.v1.author.AuthorDto;
import com.romeao.bookstore.api.v1.genre.GenreDto;
import com.romeao.bookstore.api.v1.util.BaseDto;
import com.romeao.bookstore.util.Link;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@JsonPropertyOrder({BookDto.NAME_FIELD, BookDto.PRICE_FIELD, BookDto.PUBLISHER_FIELD,
        BookDto.AUTHORS_FIELD, BookDto.GENRES_FIELD, BaseDto.LINKS_FIELD})
public class BookDto extends BaseDto {
    // field names for unit tests and error generation
    static final String NAME_FIELD = "name";
    static final String PUBLISHER_FIELD = "publisher";
    static final String PRICE_FIELD = "price";
    static final String AUTHORS_FIELD = "authors";
    static final String GENRES_FIELD = "genres";

    // fields
    private final Set<AuthorDto> authors = new TreeSet<>();
    private final Set<GenreDto> genres = new TreeSet<>();
    private String name;
    private String publisher;
    private BigDecimal price;

    public static BookDto of(String name,
                             String publisher,
                             double price,
                             Iterable<AuthorDto> authors,
                             Iterable<GenreDto> genres) {
        BookDto dto = new BookDto();
        dto.name = name;
        dto.publisher = publisher;
        dto.price = BigDecimal.valueOf(price);
        authors.forEach(dto.authors::add);
        genres.forEach(dto.genres::add);
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPrice(double price) {
        setPrice(BigDecimal.valueOf(price));
    }

    public Set<AuthorDto> getAuthors() {
        return authors;
    }

    public Set<GenreDto> getGenres() {
        return genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        BookDto bookDto = (BookDto) o;
        return Objects.equals(name, bookDto.name) &&
                Objects.equals(publisher, bookDto.publisher) &&
                Objects.equals(price, bookDto.price) &&
                authors.size() == bookDto.authors.size() &&
                authors.containsAll(bookDto.authors) &&
                genres.size() == bookDto.genres.size() &&
                genres.containsAll(bookDto.genres) &&
                getLinks().size() == bookDto.getLinks().size() &&
                getLinks().containsAll(bookDto.getLinks());
    }

    @Override
    public int hashCode() {
        int hashResult = Objects.hash(name, publisher, price);

        // Authors, genres, and links should affect the hash, but order should not matter
        for (AuthorDto author : authors) {
            hashResult ^= author.hashCode();
        }
        for (GenreDto genre : genres) {
            hashResult ^= genre.hashCode();
        }
        for (Link link : getLinks()) {
            hashResult ^= link.hashCode();
        }

        return hashResult;
    }
}
