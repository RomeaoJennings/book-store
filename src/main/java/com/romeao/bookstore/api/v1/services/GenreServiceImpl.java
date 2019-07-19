package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.mappers.GenreSummaryMapper;
import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import com.romeao.bookstore.repositories.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreSummaryMapper genreSummaryMapper = GenreSummaryMapper.INSTANCE;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreSummaryDto> summarizeAll() {
        return genreRepository.findAll(Sort.by("name")).stream()
                .map(genreSummaryMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<GenreSummaryDto> summarizeAll(int pageNum, int pageLimit) {
        PageRequest page = PageRequest.of(pageNum, pageLimit, Sort.by("name"));
        return genreRepository.findAll(page).map(genreSummaryMapper::toDto);
    }
}
