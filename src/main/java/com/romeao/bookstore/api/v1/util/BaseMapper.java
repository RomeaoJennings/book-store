package com.romeao.bookstore.api.v1.util;

import com.romeao.bookstore.domain.BaseEntity;

public interface BaseMapper<E extends BaseEntity, D extends BaseDto> {

    E toEntity(D dto);

    D toDto(E entity);
}
