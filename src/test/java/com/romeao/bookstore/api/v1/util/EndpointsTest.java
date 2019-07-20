package com.romeao.bookstore.api.v1.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EndpointsTest {
    private static final String BASE_URL = "/baseUrl";
    private static final String PARAM_ONE_KEY = "pageSize";
    private static final String PARAM_ONE_VAL = "5";
    private static final String PARAM_TWO_KEY = "pageNumber";
    private static final String PARAM_TWO_VAL = "3";

    private static final int PAGE_NUMBER = 3;
    private static final int PAGE_SIZE = 5;

    private Map<String, String> params;

    @BeforeEach
    void setUp() {
        params = new HashMap<>(2);
        params.put(PARAM_ONE_KEY, PARAM_ONE_VAL);
        params.put(PARAM_TWO_KEY, PARAM_TWO_VAL);
    }

    @Test
    void testAddParams() {
        // when
        String url = Endpoints.addParams(BASE_URL, params);

        // then
        assertNotNull(url);
        assertThat(url, anyOf(
                is("/baseUrl?pageSize=5&pageNumber=3"),
                is("/baseUrl?pageNumber=3&pageSize=5")));
    }

    @Test
    void testAddParams_withEmptyParameterList() {
        // when
        String url = Endpoints.addParams(BASE_URL, new HashMap<>());

        // then
        assertNotNull(url);
        assertEquals(BASE_URL, url);
    }

    @Test
    void testAddPathVariables() {
        // given
        String var1 = "pizza";
        String var2 = "pepperoni";

        // when
        String url = Endpoints.addPathVariables(BASE_URL, var1, var2);

        // then
        assertNotNull(url);
        assertEquals("/baseUrl/pizza/pepperoni", url);
    }

    @Test
    void testAddPathVariables_withEmptyList() {
        // when
        String url = Endpoints.addPathVariables(BASE_URL);

        // then
        assertNotNull(url);
        assertEquals(BASE_URL, url);
    }

    @Test
    void testAddPageNumberAndPageSize() {
        // when
        String url = Endpoints.addPageNumberAndPageSize(BASE_URL, PAGE_NUMBER, PAGE_SIZE);
        String pageSizeFirst = BASE_URL + "?" + Endpoints.PAGE_SIZE_PARAM + "=" + PAGE_SIZE +
                "&" + Endpoints.PAGE_NUM_PARAM + "=" + PAGE_NUMBER;

        String pageNumberFirst = BASE_URL + "?" + Endpoints.PAGE_NUM_PARAM + "=" + PAGE_NUMBER +
                "&" + Endpoints.PAGE_SIZE_PARAM + "=" + PAGE_SIZE;

        // then
        assertNotNull(url);
        assertThat(url, anyOf(
                is(pageSizeFirst),
                is(pageNumberFirst)));
    }
}