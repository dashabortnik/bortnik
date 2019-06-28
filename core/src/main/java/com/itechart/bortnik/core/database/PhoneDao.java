package com.itechart.bortnik.core.database;

import com.itechart.bortnik.core.domain.Phone;

import java.util.List;

public interface PhoneDao extends BaseEntityDao<Phone>{

    //all phones of one contact id
    public List<Phone> readAllById(int id);
}
