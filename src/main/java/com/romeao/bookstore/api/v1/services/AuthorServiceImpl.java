package com.romeao.bookstore.api.v1.services;

import com.romeao.bookstore.api.v1.mappers.AuthorMapper;
import com.romeao.bookstore.api.v1.models.AuthorDto;
import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.repositories.AuthorRepository;
import com.romeao.bookstore.util.Link;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final AuthorMapper authorMapper = AuthorMapper.INSTANCE;
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private static AuthorDto convertToDtoWithSelfLink(Author entity) {
        if (entity == null) { return null; }
        AuthorDto dto = authorMapper.toDto(entity);
        dto.getLinks().add(new Link("self", Endpoints.Author.byAuthorId(entity.getId())));
        return dto;
    }

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll(Sort.by("name")).stream()
                .map(AuthorServiceImpl::convertToDtoWithSelfLink)
                .collect(Collectors.toList());
    }
}
