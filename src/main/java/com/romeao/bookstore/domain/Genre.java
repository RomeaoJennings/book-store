package com.romeao.bookstore.domain;

import com.romeao.bookstore.domain.builders.GenreBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Genre genre = (Genre) o;
        return Objects.equals(getId(), genre.getId()) &&
                Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId()) ^
                Objects.hash(name);
    }
}
