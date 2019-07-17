package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Genre;

public class GenreBuilder {
    private final Genre genre = new Genre();

    public Genre build() { return genre; }

    public GenreBuilder withId(Integer id) {
        genre.setId(id);
        return this;
    }

    public GenreBuilder withName(String name) {
        genre.setName(name);
        return this;
    }
}
