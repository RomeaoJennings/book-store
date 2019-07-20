package com.romeao.bookstore.api.v1.author;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.util.ResourceMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@JsonPropertyOrder({"meta", "authors"})
public class AuthorDtoList implements Serializable {
    private List<AuthorDto> authors = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResourceMeta meta;

    public static AuthorDtoList of(Iterable<AuthorDto> authors, ResourceMeta meta) {
        AuthorDtoList result = new AuthorDtoList();
        result.authors.addAll(StreamSupport.stream(authors.spliterator(), false)
                .collect(Collectors.toList()));
        result.meta = meta;
        return result;
    }

    public static AuthorDtoList of(Iterable<AuthorDto> authors) {
        return of(authors, null);
    }

    public List<AuthorDto> getAuthors() {
        return authors;
    }

    public ResourceMeta getMeta() {
        return meta;
    }
}
