package com.romeao.bookstore.api.v1.author;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.domain.Author;
import com.romeao.bookstore.repositories.AuthorRepository;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Sort DEFAULT_SORT = Sort.by("lastName", "firstName");
    private static final AuthorMapper authorMapper = AuthorMapper.INSTANCE;
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private static AuthorDto convertToDtoWithSelfLink(Author entity) {
        if (entity == null) { return null; }
        AuthorDto dto = authorMapper.toDto(entity);
        dto.getLinks().add(new Link(LinkNames.SELF, Endpoints.Author.byAuthorId(entity.getId())));
        return dto;
    }

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll(DEFAULT_SORT).stream()
                .map(AuthorServiceImpl::convertToDtoWithSelfLink)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorDto> findAll(int pageNum, int pageLimit) {
        PageRequest page = PageRequest.of(pageNum, pageLimit, DEFAULT_SORT);
        return authorRepository.findAll(page).map(AuthorServiceImpl::convertToDtoWithSelfLink);
    }

    @Override
    public AuthorDto findById(int authorId) {
        return convertToDtoWithSelfLink(authorRepository.findById(authorId).orElse(null));
    }

    @Override
    public void deleteById(int genreId) {
        authorRepository.deleteById(genreId);
    }

}
