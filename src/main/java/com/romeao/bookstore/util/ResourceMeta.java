package com.romeao.bookstore.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder({"pageNumber", "pageSize", "totalPages",
        "totalElements", "previousUrl", "nextUrl"})
public class ResourceMeta implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalElements;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageSize;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String previousUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nextUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPages;

    public static ResourceMetaBuilder builder() {
        return new ResourceMetaBuilder();
    }

    public Long getTotalElements() {
        return totalElements;
    }

    void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    void setPageNumber(Integer page) {
        this.pageNumber = page;
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

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ResourceMeta that = (ResourceMeta) o;
        return Objects.equals(totalElements, that.totalElements) &&
                Objects.equals(pageSize, that.pageSize) &&
                Objects.equals(pageNumber, that.pageNumber) &&
                Objects.equals(previousUrl, that.previousUrl) &&
                Objects.equals(nextUrl, that.nextUrl) &&
                Objects.equals(totalPages, that.totalPages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalElements, pageSize, pageNumber, previousUrl, nextUrl, totalPages);
    }
}


