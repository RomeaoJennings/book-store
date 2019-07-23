package com.romeao.bookstore.api.v1.genre;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.api.v1.util.BaseDto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder({GenreDto.NAME_FIELD, GenreDto.LINKS_FIELD})
public class GenreDto extends BaseDto implements Serializable {
    static final String NAME_FIELD = "name";

    @NotBlank
    private String name;

    public GenreDto(String name) {
        this.name = name;
    }

    public GenreDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        GenreDto genreDto = (GenreDto) o;
        if (!name.equals(genreDto.name)) { return false; }
        if (getLinks().size() != genreDto.getLinks().size()) { return false; }
        return getLinks().containsAll(genreDto.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
