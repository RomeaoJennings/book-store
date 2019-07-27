package com.romeao.bookstore.api.v1.genre;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.api.v1.util.BaseDto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@JsonPropertyOrder({GenreDto.NAME_FIELD, GenreDto.LINKS_FIELD})
public class GenreDto extends BaseDto implements Comparable<GenreDto> {
    static final String NAME_FIELD = "name";

    @NotBlank
    private String name;

    public GenreDto(Integer id, String name) {
        super(id);
        this.name = name;
    }

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

        // Use ID-based equality if available
        if (getId() != null || genreDto.getId() != null) {
            return Objects.equals(getId(), genreDto.getId());
        }

        return Objects.equals(name, genreDto.name) &&
                getLinks().size() == genreDto.getLinks().size() &&
                getLinks().containsAll(genreDto.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(GenreDto o) {
        return name.compareTo(o.name);
    }
}
