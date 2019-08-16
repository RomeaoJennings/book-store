package com.romeao.bookstore.domain;

import com.romeao.bookstore.domain.builders.AuthorBuilder;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class Author extends BaseEntity {
    @NotBlank
    private String firstName;


    @NotBlank
    private String lastName;

    public static AuthorBuilder builder() {
        return new AuthorBuilder();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Author author = (Author) o;
        return Objects.equals(getId(), author.getId()) &&
                Objects.equals(firstName, author.firstName) &&
                Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
