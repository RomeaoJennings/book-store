package com.romeao.bookstore.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {
    private String name;
    private String url;

    @JsonCreator
    public Link(@JsonProperty("name") String name, @JsonProperty("url") String url) {
        if (name == null || url == null) {
            throw new IllegalArgumentException("Both name and url must be non-null");
        }
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Link link = (Link) o;
        return name.equals(link.name) &&
                url.equals(link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
