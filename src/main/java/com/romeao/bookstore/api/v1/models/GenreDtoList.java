package com.romeao.bookstore.api.v1.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.util.ResourceMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@JsonPropertyOrder({"meta", "genres"})
public class GenreDtoList implements Serializable {
    private List<GenreDto> genres = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResourceMeta meta;

    public static GenreDtoList of(Iterable<GenreDto> genres, ResourceMeta meta) {
        GenreDtoList result = new GenreDtoList();
        result.genres.addAll(StreamSupport.stream(genres.spliterator(), false)
                .collect(Collectors.toList()));
        result.meta = meta;
        return result;
    }

    public static GenreDtoList of(Iterable<GenreDto> genres) {
        return of(genres, null);
    }

    public List<GenreDto> getGenres() {
        return genres;
    }

    public ResourceMeta getMeta() {
        return meta;
    }
}
