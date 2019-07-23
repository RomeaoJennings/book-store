package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.ErrorMessages;
import com.romeao.bookstore.errorhandling.ApiException;
import com.romeao.bookstore.errorhandling.FieldValidator;
import com.romeao.bookstore.util.ResourceMeta;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public GenreDto GetGenreById(@PathVariable String genreId) {
        // Validate genreId as valid integer
        int intGenreId = FieldValidator.validateIntField("genreId", genreId);

        GenreDto result = genreService.findById(intGenreId);
        if (result == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND);
        } else { return result; }
    }

    @DeleteMapping("/{genreId}")
    public void deleteGenreById(@PathVariable String genreId) {
        // Validate authorId as valid integer
        int intGenreId = FieldValidator.validateIntField("genreId", genreId);

        if (genreService.findById(intGenreId) == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND);
        }
        genreService.deleteById(intGenreId);
    }

    @PostMapping
    public GenreDto addGenre(@Valid @RequestBody GenreDto genre, BindingResult validation) {
        FieldValidator.doFieldValidation(validation);
        return genreService.save(genre);
    }
}
