package com.romeao.bookstore.api.v1.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.romeao.bookstore.util.Link;

import java.util.ArrayList;
import java.util.List;

public class BaseDto {
    public static final String LINKS_FIELD = "links";

    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Link> links = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public BaseDto() {}

    public BaseDto(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
