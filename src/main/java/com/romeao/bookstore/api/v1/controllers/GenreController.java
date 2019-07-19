package com.romeao.bookstore.api.v1.controllers;

import com.romeao.bookstore.api.v1.models.GenreDto;
import com.romeao.bookstore.api.v1.models.GenreDtoList;
import com.romeao.bookstore.api.v1.services.GenreService;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.util.ResourceMeta;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.Genre.URL)
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public GenreDtoList getAllGenres(@RequestParam(required = false) Integer limit,
                                     @RequestParam(required = false, defaultValue = "0") Integer pageNum) {

        // TODO: Add Error Checking for non-numeric input
        // TODO: Add Error Checking for negative numeric input

        if (limit != null) {
            Page<GenreDto> page = genreService.findAll(pageNum, limit);
            ResourceMeta meta = ResourceMeta.builder()
                    .count(page.getTotalElements())
                    .limit(limit)
                    .page(pageNum)
                    .build();

            // set applicable navigation links
            if (!page.isFirst()) {
                meta.setPreviousUrl(Endpoints.Genre.byPageAndLimit(pageNum - 1, limit));
            }
            if (!page.isLast()) {
                meta.setNextUrl(Endpoints.Genre.byPageAndLimit(pageNum + 1, limit));
            }
            return GenreDtoList.of(page.getContent(), meta);
        } else {
            // return results without paging information if request does not contain page info
            return GenreDtoList.of(genreService.findAll());
        }
    }

    @GetMapping("/{genreId}")
    public GenreDto GetGenre(@PathVariable Integer genreId) {
        // TODO: Add Error Checking for non-numeric input
        // TODO: Add Error Checking for not found IDs

        return genreService.findById(genreId);
    }

    @DeleteMapping("/{genreId}")
    public void deleteGenreById(@PathVariable Integer genreId) {
        // TODO: Add Error Checking for non-numeric input
        // TODO: Add Error Checking for not-found IDs
        // TODO: Add Error Handling for Data Constraint Exceptions

        genreService.deleteById(genreId);
    }
}
