package com.romeao.bookstore.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceMetaBuilderTest {
    private static final Long TOTAL_ELEMENTS = 10L;
    private static final Integer PAGE_SIZE = 5;
    private static final Integer PAGE_NUM = 1;
    private static final String PREV_URL = "/page?pageNumber=2";
    private static final String NEXT_URL = "/page?pageNumber=0";


    @Test
    void build_withAllParams() {
        // when
        ResourceMeta meta = ResourceMeta.builder()
                .totalElements(TOTAL_ELEMENTS)
                .pageSize(PAGE_SIZE)
                .pageNumber(PAGE_NUM)
                .previousUrl(PREV_URL)
                .nextUrl(NEXT_URL)
                .build();

        assertNotNull(meta);
        assertEquals(TOTAL_ELEMENTS, meta.getTotalElements());
        assertEquals(PAGE_SIZE, meta.getPageSize());
        assertEquals(PAGE_NUM, meta.getPageNumber());
        assertEquals(PREV_URL, meta.getPreviousUrl());
        assertEquals(NEXT_URL, meta.getNextUrl());
    }

    @Test
    void fromPage() {
    }

}