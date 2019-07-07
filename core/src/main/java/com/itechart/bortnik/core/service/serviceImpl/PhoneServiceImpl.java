package com.itechart.bortnik.core.service.serviceImpl;

import com.itechart.bortnik.core.database.PhoneDao;
import com.itechart.bortnik.core.database.daoImpl.PhoneDaoImpl;
import com.itechart.bortnik.core.domain.Phone;
import com.itechart.bortnik.core.service.PhoneService;

import java.util.List;

public class PhoneServiceImpl implements PhoneService {

    private PhoneDao phoneDao;

    public PhoneServiceImpl() {
        phoneDao = PhoneDaoImpl.getInstance();
    }

    @Override
    public Phone save(Phone entity) {
        return phoneDao.insert(entity);
    }

    @Override
    public Phone update(Phone entity) {
        return phoneDao.update(entity);
    }

    @Override
    public void remove(int id) {
        phoneDao.delete(id);
    }

    @Override
    public List<Phone> findAllPhonesByContactId(int id) {
        return phoneDao.readAllById(id);
    }

}
