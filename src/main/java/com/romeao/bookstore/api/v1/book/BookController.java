package com.romeao.bookstore.api.v1.book;

import com.romeao.bookstore.api.v1.util.Endpoints;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.Book.URL)
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public BookDtoList getAllBooks() {
        return BookDtoList.of(bookService.findAll());
    }
}
