package com.romeao.bookstore.api.v1.genre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GenreService {
    Sort DEFAULT_SORT = Sort.by(GenreDto.NAME_FIELD);

    List<GenreDto> findAll();

    Page<GenreDto> findAll(int pageNum, int pageLimit);

    void deleteById(int genreId);

    GenreDto findById(int genreId);

    GenreDto findByName(String genreName);

    GenreDto save(GenreDto dto);
}
