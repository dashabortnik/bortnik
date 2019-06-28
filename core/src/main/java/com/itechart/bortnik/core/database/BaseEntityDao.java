package com.itechart.bortnik.core.database;

import com.itechart.bortnik.core.domain.BaseEntity;

public interface BaseEntityDao <T extends BaseEntity>{

    T insert(T entity);
    T update(T entity);
    void delete(int id);

}
