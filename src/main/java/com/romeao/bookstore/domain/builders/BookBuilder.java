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

    public BookBuilder withId(Integer id) {
        book.setId(id);
        return this;
    }

    public BookBuilder withName(String name) {
        book.setName(name);
        return this;
    }

    public BookBuilder withPrice(double price) {
        return withPrice(BigDecimal.valueOf(price));
    }

    public BookBuilder withPrice(BigDecimal price) {
        book.setPrice(price);
        return this;
    }

    public BookBuilder withPublisher(String publisher) {
        book.setPublisher(publisher);
        return this;
    }

    public BookBuilder withGenres(Iterable<Genre> genres) {
        Set<Genre> result = new HashSet<>(StreamSupport.stream(genres.spliterator(), false)
                .collect(Collectors.toSet()));
        book.setGenres(result);
        return this;
    }

    public BookBuilder withGenres(Genre... genres) {
        return withGenres(Arrays.asList(genres));
    }

    public BookBuilder withAuthors(Iterable<Author> authors) {
        Set<Author> result = new HashSet<>();
        for (Author author : authors) {
            author.getBooks().add(book);
            result.add(author);
        }
        book.setAuthors(result);
        return this;
    }

    public BookBuilder withAuthors(Author... authors) {
        return withAuthors(Arrays.asList(authors));
    }
}
