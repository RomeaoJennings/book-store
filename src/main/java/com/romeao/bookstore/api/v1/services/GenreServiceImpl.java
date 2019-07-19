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

    private static final GenreMapper genreMapper = GenreMapper.INSTANCE;
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    private static GenreDto convertToDtoWithSelfLink(Genre entity) {
        if (entity == null) { return null; }
        GenreDto dto = genreMapper.toDto(entity);
        dto.getLinks().add(new Link("self", Endpoints.Genre.byGenreId(entity.getId())));
        return dto;
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll(Sort.by("name")).stream()
                .map(GenreServiceImpl::convertToDtoWithSelfLink)
                .collect(Collectors.toList());
    }

    @Override
    public Page<GenreDto> findAll(int pageNum, int pageLimit) {
        PageRequest page = PageRequest.of(pageNum, pageLimit, Sort.by("name"));
        return genreRepository.findAll(page).map(GenreServiceImpl::convertToDtoWithSelfLink);
    }

    @Override
    public void deleteById(int genreId) {
        genreRepository.deleteById(genreId);
    }

    @Override
    public GenreDto findById(int genreId) {
        return convertToDtoWithSelfLink(genreRepository.findById(genreId).orElse(null));
    }
}
