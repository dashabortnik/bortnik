package com.itechart.bortnik.core.database;

import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.dto.SearchContactDTO;

import java.util.List;

public interface ContactDao extends BaseEntityDao<Contact>{

    List<Contact> readAll(int offset, int limit);
    List<Contact> readByCriteria(SearchContactDTO search, int offset, int limit);
    Contact readById(int id);
    List<Contact> readByBirthday();
    int countContacts();
    String readEmailById(int id);
    int countContactsByCriteria(SearchContactDTO searchedContact);
}
