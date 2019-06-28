package com.itechart.bortnik.core.service.serviceImpl;

import com.itechart.bortnik.core.database.ContactDao;
import com.itechart.bortnik.core.database.daoImpl.ContactDaoImpl;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.service.ContactService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ContactServiceImpl implements ContactService {

    private ContactDao contactDaoImpl;

    public ContactServiceImpl() {
        contactDaoImpl = new ContactDaoImpl();
    }

    @Override
    public Contact save(Contact entity) {
        return contactDaoImpl.insert(entity);
    }

    @Override
    public Contact update(Contact entity) { return contactDaoImpl.update(entity);}

    @Override
    public void remove(int id) {
        contactDaoImpl.delete(id);
    }

    @Override
    public List<Contact> findAllContacts() {
        return contactDaoImpl.readAll();
    }

    @Override
    public List<Contact> findContactsByCriteria(Contact search) {

        return contactDaoImpl.readByCriteria(search);
    }

    @Override
    public Contact findContactById(int id) {

        return contactDaoImpl.readById(id);
    }

    @Override
    public List<Contact> findContactByBirthday(Date birthday) {

        return contactDaoImpl.readByBirthday(birthday);
    }

}
