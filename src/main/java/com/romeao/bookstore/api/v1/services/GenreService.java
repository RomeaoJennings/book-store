package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.GenreDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();

    Page<GenreDto> findAll(int pageNum, int pageLimit);
}
