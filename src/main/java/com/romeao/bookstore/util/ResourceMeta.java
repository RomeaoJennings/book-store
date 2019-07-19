package com.romeao.bookstore.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class ResourceMeta implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer limit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer page;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String previousUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nextUrl;

    public static ResourceMetaBuilder builder() {
        return new ResourceMetaBuilder();
    }

    public Long getCount() {
        return count;
    }

    void setCount(Long count) {
        this.count = count;
    }

    public Integer getLimit() {
        return limit;
    }

    void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    void setPage(Integer page) {
        this.page = page;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}


