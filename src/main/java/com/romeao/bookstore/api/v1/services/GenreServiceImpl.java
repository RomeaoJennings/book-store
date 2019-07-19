package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.mappers.GenreMapper;
import com.romeao.bookstore.api.v1.models.GenreDto;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.domain.Genre;
import com.romeao.bookstore.repositories.GenreRepository;
import com.romeao.bookstore.util.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private static final GenreMapper GENRE_MAPPER = GenreMapper.INSTANCE;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    private static GenreDto convertToDtoWithSelfLink(Genre entity) {
        GenreDto dto = GENRE_MAPPER.toDto(entity);
        dto.getLinks().add(new Link("self", Endpoints.Genre.byGenreId(entity.getId())));
        return dto;
    }

    public List<GenreDto> findAll() {
        return genreRepository.findAll(Sort.by("name")).stream()
                .map(GenreServiceImpl::convertToDtoWithSelfLink)
                .collect(Collectors.toList());
    }

    public Page<GenreDto> findAll(int pageNum, int pageLimit) {
        PageRequest page = PageRequest.of(pageNum, pageLimit, Sort.by("name"));
        return genreRepository.findAll(page).map(GenreServiceImpl::convertToDtoWithSelfLink);
    }
}
