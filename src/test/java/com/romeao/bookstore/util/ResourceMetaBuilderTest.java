package com.romeao.bookstore.util;

import com.romeao.bookstore.api.v1.genre.GenreDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceMetaBuilderTest {
    private static final Long TOTAL_ELEMENTS = 10L;
    private static final Integer PAGE_SIZE = 5;
    private static final Integer PAGE_NUM = 1;
    private static final Integer TOTAL_PAGES = 2;
    private static final String PREV_URL = "/page?pageNumber=2";
    private static final String NEXT_URL = "/page?pageNumber=0";

    // Used for testing url mapping logic
    private static final String TEST_MAP_TEMPLATE = "pageNumber=%s&pageSize=%s";
    private static final String TEST_MAP_PREV = "pageNumber=0&pageSize=5";
    private static final String TEST_MAP_NEXT = "pageNumber=2&pageSize=5";

    @Mock
    private Page<GenreDto> mockPage;

    @Test
    void build_withAllParams() {
        // when
        ResourceMeta meta = ResourceMeta.builder()
                .totalElements(TOTAL_ELEMENTS)
                .pageSize(PAGE_SIZE)
                .pageNumber(PAGE_NUM)
                .previousUrl(PREV_URL)
                .nextUrl(NEXT_URL)
                .totalPages(TOTAL_PAGES)
                .build();

        assertNotNull(meta);
        assertEquals(TOTAL_ELEMENTS, meta.getTotalElements());
        assertEquals(PAGE_SIZE, meta.getPageSize());
        assertEquals(PAGE_NUM, meta.getPageNumber());
        assertEquals(PREV_URL, meta.getPreviousUrl());
        assertEquals(NEXT_URL, meta.getNextUrl());
    }

    @Test
    void fromPage_withPrevAndNext() {
        // given
        initMockPage(false, false);

        // when
        ResourceMeta meta = ResourceMeta.builder()
                .fromPage(mockPage, this::testMapper)
                .build();

        // then
        assertCorrectMeta(meta, false, false);
    }

    @Test
    void fromPage_withPrevOnly() {
        // given
        initMockPage(false, true);

        // when
        ResourceMeta meta = ResourceMeta.builder()
                .fromPage(mockPage, this::testMapper)
                .build();

        // then
        assertCorrectMeta(meta, false, true);
    }

    @Test
    void fromPage_withNextOnly() {
        // given
        initMockPage(true, false);

        // when
        ResourceMeta meta = ResourceMeta.builder()
                .fromPage(mockPage, this::testMapper)
                .build();

        // then
        assertCorrectMeta(meta, true, false);
    }

    private void initMockPage(boolean isFirst, boolean isLast) {
        when(mockPage.getNumber()).thenReturn(PAGE_NUM);
        when(mockPage.getSize()).thenReturn(PAGE_SIZE);
        when(mockPage.getTotalPages()).thenReturn(TOTAL_PAGES);
        when(mockPage.getTotalElements()).thenReturn(TOTAL_ELEMENTS);
        when(mockPage.isFirst()).thenReturn(isFirst);
        when(mockPage.isLast()).thenReturn(isLast);
    }

    private void assertCorrectMeta(ResourceMeta meta, boolean isFirst, boolean isLast) {
        assertNotNull(meta);
        assertEquals(TOTAL_ELEMENTS, meta.getTotalElements());
        assertEquals(PAGE_SIZE, meta.getPageSize());
        assertEquals(PAGE_NUM, meta.getPageNumber());
        assertEquals(TOTAL_PAGES, meta.getTotalPages());
        assertEquals(!isFirst ? TEST_MAP_PREV : null, meta.getPreviousUrl());
        assertEquals(!isLast ? TEST_MAP_NEXT : null, meta.getNextUrl());
    }

    private String testMapper(Integer pageNumber, Integer pageSize) {
        return String.format(TEST_MAP_TEMPLATE, pageNumber, pageSize);
    }

}