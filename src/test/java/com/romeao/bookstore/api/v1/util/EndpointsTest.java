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
    private static final String PARAM_ONE_KEY = "limit";
    private static final String PARAM_ONE_VAL = "5";
    private static final String PARAM_TWO_KEY = "pageNum";
    private static final String PARAM_TWO_VAL = "3";

    private static final int PAGE_NUM = 3;
    private static final int LIMIT = 5;

    private static Map<String, String> params;

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

        assertNotNull(url);
        assertThat(url, anyOf(
                is("/baseUrl?limit=5&pageNum=3"),
                is("/baseUrl?pageNum=3&limit=5")));
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
    void testAddPageNumAndLimit() {
        // when
        String url = Endpoints.addPageNumAndLimit(BASE_URL, PAGE_NUM, LIMIT);
        String limitFirst = new StringBuilder(BASE_URL)
                .append("?")
                .append(Endpoints.LIMIT_PARAM)
                .append("=")
                .append(LIMIT)
                .append("&")
                .append(Endpoints.PAGE_NUM_PARAM)
                .append("=")
                .append(PAGE_NUM)
                .toString();

        String pageNumFirst = new StringBuilder(BASE_URL)
                .append("?")
                .append(Endpoints.PAGE_NUM_PARAM)
                .append("=")
                .append(PAGE_NUM)
                .append("&")
                .append(Endpoints.LIMIT_PARAM)
                .append("=")
                .append(LIMIT)
                .toString();


        // then
        assertNotNull(url);
        assertThat(url, anyOf(
                is(limitFirst),
                is(pageNumFirst)));
    }
}