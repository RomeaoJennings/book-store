package com.romeao.bookstore.api.v1.author;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.api.v1.util.BaseDto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@JsonPropertyOrder({AuthorDto.FIRST_NAME_FIELD, AuthorDto.LAST_NAME_FIELD, AuthorDto.LINKS_FIELD})
public class AuthorDto extends BaseDto implements Comparable<AuthorDto> {
    static final String FIRST_NAME_FIELD = "firstName";
    static final String LAST_NAME_FIELD = "lastName";

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public AuthorDto(Integer id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AuthorDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AuthorDto() { }

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
        // Use ID-based equality if it is available
        if (getId() != null || authorDto.getId() != null) {
            return Objects.equals(getId(), authorDto.getId());
        }

        return Objects.equals(firstName, authorDto.firstName) &&
                Objects.equals(lastName, authorDto.lastName) &&
                getLinks().size() == authorDto.getLinks().size() &&
                getLinks().containsAll(authorDto.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, getLinks());
    }

    @Override
    public int compareTo(AuthorDto o) {
        int last = lastName.compareTo(o.lastName);
        return last == 0 ? firstName.compareTo(o.firstName) : last;
    }
}
