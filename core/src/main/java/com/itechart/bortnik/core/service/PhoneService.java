package com.itechart.bortnik.core.service;

import com.itechart.bortnik.core.domain.Phone;

import java.util.List;

public interface PhoneService extends BaseEntityService<Phone, Phone>{

    List<Phone> findAllPhonesByContactId(int id);

}
