package com.itechart.bortnik.core.service;

import com.itechart.bortnik.core.domain.BaseEntity;

public interface BaseEntityService <T extends BaseEntity>{

    T save(T entity);
    T update(T entity);
    void remove(int id);
}
