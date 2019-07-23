package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Author;

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
}
