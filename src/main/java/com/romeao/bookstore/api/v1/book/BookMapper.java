package com.romeao.bookstore.api.v1.book;

import com.romeao.bookstore.api.v1.author.AuthorMapper;
import com.romeao.bookstore.api.v1.genre.GenreMapper;
import com.romeao.bookstore.api.v1.util.BaseMapper;
import com.romeao.bookstore.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {GenreMapper.class, AuthorMapper.class})
public interface BookMapper extends BaseMapper<Book, BookDto> {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
}
