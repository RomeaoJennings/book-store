package com.romeao.bookstore.api.v1.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuthorService {
    Sort DEFAULT_SORT = Sort.by(AuthorDto.LAST_NAME_FIELD, AuthorDto.FIRST_NAME_FIELD);

    List<AuthorDto> findAll();

    Page<AuthorDto> findAll(int pageNum, int pageLimit);

    AuthorDto findById(int authorId);

    void deleteById(int genreId);
}
