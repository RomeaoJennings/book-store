package com.romeao.bookstore.api.v1.mappers;

import com.romeao.bookstore.api.v1.models.AuthorDto;
import com.romeao.bookstore.domain.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper extends BaseMapper<Author, AuthorDto> {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);
}
