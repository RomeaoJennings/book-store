package com.romeao.bookstore.domain;

import com.romeao.bookstore.domain.builders.AuthorBuilder;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

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
}
