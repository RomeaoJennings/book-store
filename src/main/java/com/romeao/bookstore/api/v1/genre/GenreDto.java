package com.romeao.bookstore.api.v1.genre;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.romeao.bookstore.api.v1.util.BaseDto;

import java.io.Serializable;

@JsonPropertyOrder({"name", "links"})
public class GenreDto extends BaseDto implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}