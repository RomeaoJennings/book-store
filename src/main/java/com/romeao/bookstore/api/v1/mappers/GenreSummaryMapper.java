package com.romeao.bookstore.api.v1.mappers;

import com.romeao.bookstore.api.v1.models.GenreSummaryDto;
import com.romeao.bookstore.domain.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreSummaryMapper extends BaseMapper<Genre, GenreSummaryDto> {
    GenreSummaryMapper INSTANCE = Mappers.getMapper(GenreSummaryMapper.class);
}
