package com.romeao.bookstore.util;

import org.springframework.data.domain.Page;

import java.util.function.BiFunction;

public class ResourceMetaBuilder {
    private final ResourceMeta meta = new ResourceMeta();

    /**
     * Creates a ResourceMeta object from page parameters.
     *
     * @param page          The page of data to generate the ResourceMeta from.
     * @param navLinkMapper A function to map endpoints.  It's parameters must be ordered
     *                      pageNumber, then pageSize.  Maps the parameters into the relevant Url
     *                      for previous and next URLs.
     * @return The built ResourceMeta object
     */
    public ResourceMetaBuilder fromPage(
            Page<?> page,
            BiFunction<Integer, Integer, String> navLinkMapper) {

        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        meta.setTotalElements(page.getTotalElements());
        meta.setPageSize(pageSize);
        meta.setPageNumber(pageNumber);
        meta.setTotalPages(page.getTotalPages());

        // set applicable navigation links
        if (!page.isFirst()) {
            meta.setPreviousUrl(navLinkMapper.apply(pageNumber - 1, pageSize));
        }
        if (!page.isLast()) {
            meta.setNextUrl(navLinkMapper.apply(pageNumber + 1, pageSize));
        }
        return this;
    }

    public ResourceMeta build() {
        return meta;
    }

    public ResourceMetaBuilder totalElements(Long count) {
        meta.setTotalElements(count);
        return this;
    }

    public ResourceMetaBuilder pageSize(Integer limit) {
        meta.setPageSize(limit);
        return this;
    }

    public ResourceMetaBuilder pageNumber(Integer page) {
        meta.setPageNumber(page);
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

    ResourceMetaBuilder totalPages(Integer totalPages) {
        meta.setTotalPages(totalPages);
        return this;
    }
}
