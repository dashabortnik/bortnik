package com.itechart.bortnik.core.service;

import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;
import com.itechart.bortnik.core.domain.dto.SearchContactDTO;

import java.util.List;

public interface ContactService extends BaseEntityService<Contact, FullContactDTO>{

    List<Contact> findAllContacts(int offset, int limit);
    List<Contact> findContactsByCriteria(SearchContactDTO search);
    FullContactDTO findContactById(int id);
    List<Contact> findContactByBirthday();
    int countAllContacts();
    String findEmailById(int id);
}
