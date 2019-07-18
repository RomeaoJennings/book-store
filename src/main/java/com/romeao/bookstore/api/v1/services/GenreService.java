package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.mappers.GenreSummaryMapper;
import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import com.romeao.bookstore.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreSummaryMapper genreSummaryMapper = GenreSummaryMapper.INSTANCE;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreSummaryDto> summarizeAll() {
        return genreRepository.findAll().stream()
                .map(genreSummaryMapper::toDto)
                .collect(Collectors.toList());
    }
}
