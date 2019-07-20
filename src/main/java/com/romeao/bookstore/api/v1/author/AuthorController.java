package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
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
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum) {

        // TODO: Add Error Checking for non-numeric input
        // TODO: Add Error Checking for negative numeric input

        if (limit != null) {
            Page<AuthorDto> page = authorService.findAll(pageNum, limit);
            ResourceMeta meta = ResourceMeta.builder()
                    .count(page.getTotalElements())
                    .limit(limit)
                    .page(pageNum)
                    .build();

            // set applicable navigation links
            if (!page.isFirst()) {
                meta.setPreviousUrl(Endpoints.Author.byPageAndLimit(pageNum - 1, limit));
            }
            if (!page.isLast()) {
                meta.setNextUrl(Endpoints.Author.byPageAndLimit(pageNum + 1, limit));
            }
            return AuthorDtoList.of(page.getContent(), meta);
        } else {
            // return results without paging information if request does not contain limits
            return AuthorDtoList.of(authorService.findAll());
        }

    }

    @GetMapping("/{authorId}")
    public AuthorDto GetAuthor(@PathVariable Integer authorId) {
        // TODO: Add Error Checking for non-numeric input
        // TODO: Add Error Checking for not found IDs

        return authorService.findById(authorId);
    }

    @DeleteMapping("/{authorId}")
    public void deleteAuthorById(@PathVariable Integer authorId) {
        // TODO: Add Error Checking for non-numeric input
        // TODO: Add Error Checking for not-found IDs
        // TODO: Add Error Handling for Data Constraint Exceptions

        authorService.deleteById(authorId);
    }
}
