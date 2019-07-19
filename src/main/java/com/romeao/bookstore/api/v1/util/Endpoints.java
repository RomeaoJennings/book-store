package com.romeao.bookstore.api.v1.util;

import java.util.HashMap;
import java.util.Map;

public class Endpoints {
    static final String PAGE_NUM_PARAM = "pageNum";
    static final String LIMIT_PARAM = "limit";
    private static final String BASE_API = "/api/v1";

    static String addParams(String baseUrl, Map<String, String> params) {
        if (params.size() == 0) {
            throw new IllegalArgumentException("You must provide at least one parameter.");
        }

        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append('?');
        params.forEach((key, value) -> {
            builder.append(key);
            builder.append('=');
            builder.append(value);
            builder.append("&");
        });
        // remove extra & after last parameter
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    static String addPageNumAndLimit(String baseUrl, int pageNum, int limit) {
        Map<String, String> params = new HashMap<>(2);
        params.put(PAGE_NUM_PARAM, String.valueOf(pageNum));
        params.put(LIMIT_PARAM, String.valueOf(limit));
        return addParams(baseUrl, params);
    }

    public static class Genre {
        public static final String URL = BASE_API + "/genres";

        public static String byPageAndLimit(int pageNum, int limit) {
            return addPageNumAndLimit(URL, pageNum, limit);
        }
    }
}
