package com.sgedts.base.mapper;

import java.util.List;

/**
 * Contract for a generic bean to entity mapper.
 *
 * @param <B> - Bean type parameter.
 * @param <E> - Entity type parameter.
 */
public interface EntityMapper<B, E> {

    E toEntity(B bean);

    B toBean(E entity);

    List <E> toEntity(List<B> beanList);

    List <B> toBean(List<E> entityList);
}
