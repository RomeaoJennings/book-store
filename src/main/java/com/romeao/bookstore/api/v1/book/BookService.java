package com.romeao.bookstore.api.v1.book;

import java.util.List;

public interface BookService {
    List<BookDto> findAll();
}
