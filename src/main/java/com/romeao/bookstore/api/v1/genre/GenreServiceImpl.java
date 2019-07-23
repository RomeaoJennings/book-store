package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.api.v1.util.ErrorMessages;
import com.romeao.bookstore.domain.Genre;
import com.romeao.bookstore.errorhandling.ApiError;
import com.romeao.bookstore.errorhandling.ApiException;
import com.romeao.bookstore.errorhandling.ApiValidationError;
import com.romeao.bookstore.repositories.GenreRepository;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private static final Sort DEFAULT_SORT = Sort.by(GenreDto.NAME_FIELD);
    private static final GenreMapper genreMapper = GenreMapper.INSTANCE;
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    private static GenreDto convertToDtoWithSelfLink(Genre entity) {
        if (entity == null) { return null; }
        GenreDto dto = genreMapper.toDto(entity);
        dto.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(entity.getId())));
        return dto;
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll(DEFAULT_SORT).stream()
                .map(GenreServiceImpl::convertToDtoWithSelfLink)
                .collect(Collectors.toList());
    }

    @Override
    public Page<GenreDto> findAll(int pageNum, int pageLimit) {
        PageRequest page = PageRequest.of(pageNum, pageLimit, DEFAULT_SORT);
        return genreRepository.findAll(page).map(GenreServiceImpl::convertToDtoWithSelfLink);
    }

    @Override
    public GenreDto findById(int genreId) {
        return convertToDtoWithSelfLink(genreRepository.findById(genreId).orElse(null));
    }

    @Override
    public GenreDto findByName(String genreName) {
        return convertToDtoWithSelfLink(
                genreRepository.findByNameIgnoreCase(genreName).orElse(null));
    }

    @Override
    public void deleteById(int genreId) {
        if (findById(genreId) == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND);
        }
        genreRepository.deleteById(genreId);
    }

    @Override
    public GenreDto save(GenreDto dto) {
        if (findByName(dto.getName()) != null) {
            ApiValidationError validationErr = new ApiValidationError(GenreDto.NAME_FIELD,
                    ErrorMessages.RESOURCE_EXISTS, dto.getName());
            ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                    ErrorMessages.RESOURCE_EXISTS);
            apiError.getValidationErrors().add(validationErr);
            throw new ApiException(apiError);
        }
        return convertToDtoWithSelfLink(genreRepository.save(genreMapper.toEntity(dto)));
    }
}
