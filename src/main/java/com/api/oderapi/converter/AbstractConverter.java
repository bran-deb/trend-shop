package com.api.oderapi.converter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConverter<E, D> {

    public abstract D fromEntity(E entity);

    public abstract E fromDTO(D dto);

    // is creating a list of `ProductDTO` objects from a list of Product` objects.
    public List<D> fromEntity(List<E> entities) {
        return entities.stream()
                .map(e -> fromEntity(e))
                .collect(Collectors.toList());
    }

    // is creating a list of `Product` objects from a list of ProductDTO` objects.
    public List<E> fromDTO(List<D> dtos) {
        return dtos.stream()
                .map(dto -> fromDTO(dto))
                .collect(Collectors.toList());
    }
}
