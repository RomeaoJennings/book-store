package com.romeao.bookstore.domain;

import com.romeao.bookstore.domain.builders.GenreBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Genre extends BaseEntity {

    @Column(unique = true)
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GenreBuilder builder() {
        return new GenreBuilder();
    }
}
