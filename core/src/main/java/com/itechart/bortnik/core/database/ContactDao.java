package com.itechart.bortnik.core.database;

import com.itechart.bortnik.core.domain.Contact;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ContactDao extends BaseEntityDao<Contact>{

    List<Contact> readAll();
    List<Contact> readByCriteria(Contact search);
    Contact readById(int id);
    List<Contact> readByBirthday(Date today);
}
