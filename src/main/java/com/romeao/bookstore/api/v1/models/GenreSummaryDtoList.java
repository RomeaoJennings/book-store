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
public class GenreSummaryDtoList implements Serializable {
    private List<GenreSummaryDto> genres = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResourceMeta meta;

    public static GenreSummaryDtoList of(Iterable<GenreSummaryDto> genres, ResourceMeta meta) {
        GenreSummaryDtoList result = new GenreSummaryDtoList();
        result.genres.addAll(StreamSupport.stream(genres.spliterator(), false)
                .collect(Collectors.toList()));
        result.meta = meta;
        return result;
    }

    public static GenreSummaryDtoList of(Iterable<GenreSummaryDto> genres) {
        return of(genres, null);
    }

    public List<GenreSummaryDto> getGenres() {
        return genres;
    }

    public ResourceMeta getMeta() {
        return meta;
    }
}
