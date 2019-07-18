package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.domain.Book;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AuthorBuilder {
    private final Author author = new Author();

    public Author build() { return author; }

    public AuthorBuilder id(Integer id) {
        author.setId(id);
        return this;
    }

    public AuthorBuilder firstName(String firstName) {
        author.setFirstName(firstName);
        return this;
    }

    public AuthorBuilder lastName(String lastName) {
        author.setLastName(lastName);
        return this;
    }

    public AuthorBuilder books(Iterable<Book> books) {
        author.setBooks(StreamSupport.stream(books.spliterator(), false)
                .collect(Collectors.toSet()));
        return this;
    }

    public AuthorBuilder books(Book... books) {
        return books(Arrays.asList(books));
    }
}
