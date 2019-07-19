package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.AuthorDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();

    Page<AuthorDto> findAll(int pageNum, int pageLimit);

    AuthorDto findById(int authorId);

    void deleteById(int genreId);
}
