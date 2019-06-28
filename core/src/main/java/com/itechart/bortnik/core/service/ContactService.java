package com.itechart.bortnik.core.service;

import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ContactService extends BaseEntityService<Contact>{

    List<Contact> findAllContacts();
    List<Contact> findContactsByCriteria(Contact search);
    FullContactDTO findContactById(int id);
    List<Contact> findContactByBirthday(Date birthday);

}
