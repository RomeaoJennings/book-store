package com.romeao.bookstore.api.v1.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.romeao.bookstore.util.Link;

import java.util.ArrayList;
import java.util.List;

public class BaseDto {

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    private final List<Link> links = new ArrayList<>();
}
