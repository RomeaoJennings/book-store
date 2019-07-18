package com.romeao.bookstore.domain.builders;

import com.romeao.bookstore.domain.Genre;

public class GenreBuilder {
    private final Genre genre = new Genre();

    public Genre build() { return genre; }

    public GenreBuilder id(Integer id) {
        genre.setId(id);
        return this;
    }

    public GenreBuilder name(String name) {
        genre.setName(name);
        return this;
    }
}
