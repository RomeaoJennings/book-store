package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.models.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
