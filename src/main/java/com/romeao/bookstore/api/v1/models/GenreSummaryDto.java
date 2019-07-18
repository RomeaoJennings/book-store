package com.romeao.bookstore.api.v1.models;

public class GenreSummaryDto extends BaseDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
