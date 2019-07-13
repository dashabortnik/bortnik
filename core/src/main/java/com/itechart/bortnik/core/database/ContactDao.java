package com.itechart.bortnik.core.database;

import com.itechart.bortnik.core.domain.Contact;

import java.util.List;

public interface ContactDao extends BaseEntityDao<Contact>{

    List<Contact> readAll(int offset, int limit);
    List<Contact> readByCriteria(Contact search);
    Contact readById(int id);
    List<Contact> readByBirthday();
    int countContacts();
}
