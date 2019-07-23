package com.romeao.bookstore.api.v1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.romeao.bookstore.errorhandling.ApiError;
import com.romeao.bookstore.errorhandling.ApiValidationError;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TestUtils {
    protected static final String PAGE_NUMBER_FIELD = "pageNumber";
    protected static final String PAGE_SIZE_FIELD = "pageSize";
    private static ObjectMapper jsonMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    protected static void assertCorrectSelfLink(BaseDto dto, String selfLinkUrl) {
        assertNotNull(dto);
        List<Link> links = dto.getLinks();
        assertNotNull(links);

        boolean selfLinkExists = false;

        for (Link link : links) {
            if (link.getName().equals(LinkNames.SELF)) {
                selfLinkExists = true;
                assertEquals(selfLinkUrl, link.getUrl());
            }
        }
        assertTrue(selfLinkExists);
    }

    protected static <T> T convertJsonResponse(MvcResult result, Class<T> clazz) {
        try {
            return jsonMapper.readValue(result.getResponse().getContentAsString(), clazz);
        } catch (IOException exception) {
            return null;
        }
    }

    protected static ApiError ofMalformedIntParameter(String field, String rejectedValue) {
        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST,
                ErrorMessages.MALFORMED_PARAMETER);
        expectedError.getValidationErrors().add(new ApiValidationError(field,
                ErrorMessages.INVALID_INTEGER, rejectedValue));
        return expectedError;
    }

    protected static ApiErrorMatcher apiError(ApiError error) {
        return new ApiErrorMatcher(error);
    }

    private static final class ApiErrorMatcher implements ResultMatcher {
        private final ApiError error;

        ApiErrorMatcher(ApiError error) {
            this.error = error;
        }

        @Override
        public void match(MvcResult result) throws Exception {
            String jsonText = result.getResponse().getContentAsString();
            ApiError jsonError = jsonMapper.readValue(jsonText, ApiError.class);

            assertEquals(error.getMessage(), jsonError.getMessage());
            assertEquals(error.getStatus(), jsonError.getStatus());
            assertEquals(error.getStatusCode(), jsonError.getStatusCode());
            assertEquals(error.getDebugMessage(), jsonError.getDebugMessage());
            assertTrue(ChronoUnit.SECONDS.between(jsonError.getTimestamp(), error.getTimestamp()) < 2);
            assertEquals(error.getValidationErrors().size(),
                    jsonError.getValidationErrors().size());
            assertThat(error.getValidationErrors(),
                    containsInAnyOrder(jsonError.getValidationErrors().toArray()));
        }
    }


}
