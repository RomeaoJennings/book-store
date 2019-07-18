package com.romeao.bookstore.api.v1.mappers;

import com.romeao.bookstore.api.v1.models.BaseDto;
import com.romeao.bookstore.domain.BaseEntity;

public interface BaseMapper<E extends BaseEntity, D extends BaseDto> {

    E toEntity(D dto);

    D toDto(E entity);
}
