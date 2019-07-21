package com.romeao.bookstore.api.v1.util;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {
    public static void validateMalformedIntJson(ResultActions request,
                                                String field,
                                                String rejectedValue) throws Exception {
        request.
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        equalTo(ErrorMessages.MALFORMED_PARAMETER)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.statusCode", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.subErrors", hasSize(1)))
                .andExpect(jsonPath("$.subErrors[0].field", equalTo(field)))
                .andExpect(jsonPath("$.subErrors[0].rejectedValue",
                        equalTo(rejectedValue)))
                .andExpect(jsonPath("$.subErrors[0].message",
                        equalTo(ErrorMessages.INVALID_INTEGER)));
    }
}
