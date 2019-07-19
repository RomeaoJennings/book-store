package com.romeao.bookstore.api.v1.controllers;

import com.romeao.bookstore.api.v1.models.GenreDto;
import com.romeao.bookstore.api.v1.models.GenreDtoList;
import com.romeao.bookstore.api.v1.services.GenreService;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.util.ResourceMeta;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
