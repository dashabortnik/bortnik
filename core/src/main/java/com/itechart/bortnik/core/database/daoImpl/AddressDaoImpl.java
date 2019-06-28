package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.AddressDao;
import com.itechart.bortnik.core.domain.Address;

public class AddressDaoImpl implements AddressDao {

    public AddressDaoImpl() {
    }

    //create the only instance of class and return it afterwards
    private static class Singleton{
        private static final AddressDaoImpl INSTANCE = new AddressDaoImpl();
    }

    //DO I NEED THE CLASS?!

    public static AddressDaoImpl getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Address insert(Address entity) {
        return null;
    }

    @Override
    public Address update(Address entity) { return null; }

    @Override
    public void delete(int id) {

    }
}
