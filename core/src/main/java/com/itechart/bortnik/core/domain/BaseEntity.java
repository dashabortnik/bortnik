package com.itechart.bortnik.core.domain;

public abstract class BaseEntity {

    private int id;

    public BaseEntity(){}

    public BaseEntity(int id) {
        super();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
