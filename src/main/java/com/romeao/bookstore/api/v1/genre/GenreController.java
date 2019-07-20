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

    @GetMapping
    public GenreDtoList getAllGenres(
            @RequestParam(required = false) String pageSize,
            @RequestParam(required = false, defaultValue = "0") String pageNumber) {

        if (pageSize != null) {
            // Validate parameters for invalid integer values
            FieldValidator.doPageValidation(pageSize, pageNumber);

            // convert request parameters to integers
            int intPageSize = Integer.valueOf(pageSize);
            int intPageNumber = Integer.valueOf(pageNumber);

            // get content and meta
            Page<GenreDto> page = genreService.findAll(intPageNumber, intPageSize);
            ResourceMeta meta = ResourceMeta.builder()
                    .fromPage(page, Endpoints.Genre::byPageNumberAndPageSize)
                    .build();
            return GenreDtoList.of(page.getContent(), meta);
        } else {
            // return results without paging information if request does not contain page info
            return GenreDtoList.of(genreService.findAll());
        }
    }

    @GetMapping("/{genreId}")
    public GenreDto GetGenre(@PathVariable String genreId) {
        // Validate genreId as valid integer
        int intGenreId = FieldValidator.validateIntField("genreId", genreId);

        // TODO: Add Error Checking for not found IDs

        return genreService.findById(intGenreId);
    }

    @DeleteMapping("/{genreId}")
    public void deleteGenreById(@PathVariable String genreId) {
        // Validate genreId as valid integer
        int intGenreId = FieldValidator.validateIntField("genreId", genreId);

        // TODO: Add Error Checking for not-found IDs
        // TODO: Add Error Handling for Data Constraint Exceptions

        genreService.deleteById(intGenreId);
    }
}
