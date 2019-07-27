package com.romeao.bookstore.api.v1.book;

import com.romeao.bookstore.api.v1.util.Endpoints;
import com.romeao.bookstore.domain.Book;
import com.romeao.bookstore.repositories.BookRepository;
import com.romeao.bookstore.util.Link;
import com.romeao.bookstore.util.LinkNames;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final BookMapper mapper = BookMapper.INSTANCE;
    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    private static BookDto convertToDtoWithSelfLink(Book entity) {
        if (entity == null) { return null; }
        BookDto dto = mapper.toDto(entity);
        dto.getLinks().add(new Link(LinkNames.SELF, Endpoints.Genre.byGenreId(entity.getId())));
        return dto;
    }

    @Override
    public List<BookDto> findAll() {
        return repository.findAll().stream()
                .map(BookServiceImpl::convertToDtoWithSelfLink)
                .collect(Collectors.toList());
    }
}
