package com.romeao.bookstore.api.v1.controllers;

import com.romeao.bookstore.api.v1.services.GenreService;
import com.romeao.bookstore.api.v1.util.Endpoints;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(Endpoints.Genre.URL)
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Map<String, Object> getAllGenres() {
        Map<String, Object> result = new HashMap<>();
        result.put("genres", genreService.summarizeAll());
        return result;
    }
}
