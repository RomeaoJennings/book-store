package com.romeao.bookstore.api.v1.genre;

import com.romeao.bookstore.api.v1.util.BaseMapper;
import com.romeao.bookstore.domain.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper extends BaseMapper<Genre, GenreDto> {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);
}
