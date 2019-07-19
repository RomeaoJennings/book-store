package com.romeao.bookstore.util;

public class ResourceMetaBuilder {
    private final ResourceMeta meta = new ResourceMeta();

    public ResourceMeta build() {
        return meta;
    }

    public ResourceMetaBuilder count(Long count) {
        meta.setCount(count);
        return this;
    }

    public ResourceMetaBuilder limit(Integer limit) {
        meta.setLimit(limit);
        return this;
    }

    public ResourceMetaBuilder page(Integer page) {
        meta.setPageNum(page);
        return this;
    }

    ResourceMetaBuilder previousUrl(String previousUrl) {
        meta.setPreviousUrl(previousUrl);
        return this;
    }

    ResourceMetaBuilder nextUrl(String nextUrl) {
        meta.setNextUrl(nextUrl);
        return this;
    }
}
