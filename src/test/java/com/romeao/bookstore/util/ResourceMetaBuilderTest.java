package com.romeao.bookstore.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceMetaBuilderTest {
    private static final Long COUNT = 10L;
    private static final Integer LIMIT = 5;
    private static final Integer PAGE = 1;
    private static final String PREV_URL = "/page?pageNum=2";
    private static final String NEXT_URL = "/page?pageNum=0";


    @Test
    void build_withAllParams() {
        // when
        ResourceMeta meta = ResourceMeta.builder()
                .count(COUNT)
                .limit(LIMIT)
                .page(PAGE)
                .previousUrl(PREV_URL)
                .nextUrl(NEXT_URL)
                .build();

        assertNotNull(meta);
        assertEquals(COUNT, meta.getCount());
        assertEquals(LIMIT, meta.getLimit());
        assertEquals(PAGE, meta.getPage());
        assertEquals(PREV_URL, meta.getPreviousUrl());
        assertEquals(NEXT_URL, meta.getNextUrl());
    }

    @Test
    void fromPage() {
    }

}