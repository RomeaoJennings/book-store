package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.domain.Book;
import com.romeao.bookstore.domain.Genre;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookBuilder {
    private final Book book = new Book();

    public Book build() {
        return book;
    }

    public BookBuilder id(Integer id) {
        book.setId(id);
        return this;
    }

    public BookBuilder name(String name) {
        book.setName(name);
        return this;
    }

    public BookBuilder price(double price) {
        return price(BigDecimal.valueOf(price));
    }

    public BookBuilder price(BigDecimal price) {
        book.setPrice(price);
        return this;
    }

    public BookBuilder publisher(String publisher) {
        book.setPublisher(publisher);
        return this;
    }

    public BookBuilder genres(Iterable<Genre> genres) {
        Set<Genre> result = StreamSupport.stream(genres.spliterator(), false)
                .collect(Collectors.toSet());
        book.setGenres(result);
        return this;
    }

    public BookBuilder genres(Genre... genres) {
        return genres(Arrays.asList(genres));
    }

    public BookBuilder authors(Iterable<Author> authors) {
        Set<Author> result = new HashSet<>();
        authors.forEach(result::add);
        book.setAuthors(result);
        return this;
    }

    public BookBuilder authors(Author... authors) {
        return authors(Arrays.asList(authors));
    }
}
