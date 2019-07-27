package com.romeao.bookstore.api.v1.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.util.ResourceMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@JsonPropertyOrder({"meta", "books"})
public class BookDtoList implements Serializable {
    private List<BookDto> books = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResourceMeta meta;

    public static BookDtoList of(Iterable<BookDto> books, ResourceMeta meta) {
        BookDtoList result = new BookDtoList();
        result.books.addAll(StreamSupport.stream(books.spliterator(), false)
                .collect(Collectors.toList()));
        result.meta = meta;
        return result;
    }

    public static BookDtoList of(Iterable<BookDto> books) {
        return of(books, null);
    }

    public List<BookDto> getBooks() {
        return books;
    }

    public ResourceMeta getMeta() {
        return meta;
    }
}
