package com.romeao.bookstore.api.v1.author;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.api.v1.util.BaseDto;
import com.romeao.bookstore.util.Link;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@JsonPropertyOrder({AuthorDto.FIRST_NAME_FIELD, AuthorDto.LAST_NAME_FIELD, AuthorDto.LINKS_FIELD})
public class AuthorDto extends BaseDto {
    static final String FIRST_NAME_FIELD = "firstName";
    static final String LAST_NAME_FIELD = "lastName";

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public AuthorDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AuthorDto() {
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
        AuthorDto authorDto = (AuthorDto) o;
        if (!firstName.equals(authorDto.firstName) ||
                !lastName.equals(authorDto.lastName) ||
                getLinks().size() != authorDto.getLinks().size()) {
            return false;
        }
        for (Link link : getLinks()) {
            if (!authorDto.getLinks().contains(link)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, getLinks());
    }
}
