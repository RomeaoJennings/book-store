package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.errorhandling.FieldValidator;
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

    private static ResourceMeta buildPageMeta(int limit, int pageNum, Page<?> page) {

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
        return meta;
    }

    @GetMapping
    public GenreDtoList getAllGenres(
            @RequestParam(required = false, name = "limit") String limitStr,
            @RequestParam(required = false, defaultValue = "0", name = "pageNum") String pageNumStr) {

        if (limitStr != null) {
            int limit, pageNum;
            // Validate parameters for invalid integer values
            FieldValidator.doPageValidation(limitStr, pageNumStr);

            // convert request parameters to integers
            limit = Integer.valueOf(limitStr);
            pageNum = Integer.valueOf(pageNumStr);

            // get content and meta
            Page<GenreDto> page = genreService.findAll(pageNum, limit);
            ResourceMeta meta = buildPageMeta(limit, pageNum, page);
            return GenreDtoList.of(page.getContent(), meta);
        } else {
            // return results without paging information if request does not contain page info
            return GenreDtoList.of(genreService.findAll());
        }
    }

    @GetMapping("/{genreId}")
    public GenreDto GetGenre(@PathVariable String genreId) {
        // Validate genreId as valid integer
        int id = FieldValidator.validateIntField("genreId", genreId);

        // TODO: Add Error Checking for not found IDs

        return genreService.findById(id);
    }

    @DeleteMapping("/{genreId}")
    public void deleteGenreById(@PathVariable String genreId) {
        // Validate genreId as valid integer
        int id = FieldValidator.validateIntField("genreId", genreId);

        // TODO: Add Error Checking for not-found IDs
        // TODO: Add Error Handling for Data Constraint Exceptions

        genreService.deleteById(id);
    }
}
