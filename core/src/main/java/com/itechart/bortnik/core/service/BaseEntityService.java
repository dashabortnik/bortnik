package com.itechart.bortnik.core.service;

import com.itechart.bortnik.core.domain.BaseEntity;

public interface BaseEntityService <T, E extends BaseEntity>{

    E save(E entity);
    T update(T entity);
    void remove(int id);
}
