package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.errorhandling.FieldValidator;
import com.romeao.bookstore.util.ResourceMeta;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.Author.URL)
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public AuthorDtoList getAllAuthors(
            @RequestParam(required = false) String pageSize,
            @RequestParam(required = false, defaultValue = "0") String pageNumber) {

        if (pageSize != null) {
            // Validate parameters for invalid integer values
            FieldValidator.doPageValidation(pageSize, pageNumber);

            // convert request parameters to integers
            int intPageSize = Integer.valueOf(pageSize);
            int intPageNumber = Integer.valueOf(pageNumber);

            // get content and meta
            Page<AuthorDto> page = authorService.findAll(intPageNumber, intPageSize);
            ResourceMeta meta = ResourceMeta.builder()
                    .fromPage(page, Endpoints.Author::byPageNumberAndPageSize)
                    .build();
            return AuthorDtoList.of(page.getContent(), meta);
        } else {
            // return results without paging information if request does not contain limits
            return AuthorDtoList.of(authorService.findAll());
        }

    }

    @GetMapping("/{authorId}")
    public AuthorDto GetAuthor(@PathVariable String authorId) {
        int intAuthorId = FieldValidator.validateIntField("authorId", authorId);

        // TODO: Add Error Checking for not found IDs

        return authorService.findById(intAuthorId);
    }

    @DeleteMapping("/{authorId}")
    public void deleteAuthorById(@PathVariable String authorId) {
        int intAuthorId = FieldValidator.validateIntField("authorId", authorId);

        // TODO: Add Error Checking for not-found IDs
        // TODO: Add Error Handling for Data Constraint Exceptions

        authorService.deleteById(intAuthorId);
    }
}
