package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.DatabaseUtil;
import com.itechart.bortnik.core.database.PhoneDao;
import com.itechart.bortnik.core.domain.Phone;
import com.itechart.bortnik.core.domain.PhoneType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneDaoImpl implements PhoneDao {

    public PhoneDaoImpl() {
    }

    // create the only instance of class and return it afterwards
    private static class Singletone {
        private static final PhoneDaoImpl INSTANCE = new PhoneDaoImpl();
    }

    public static PhoneDaoImpl getInstance() {
        return Singletone.INSTANCE;
    }

    @Override
    public List<Phone> readAllById(int id) {

        List<Phone> phones = new ArrayList<>();
        String sql = "SELECT * FROM phone WHERE contact_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int phoneId = resultSet.getInt("phone_id"); // в кавычках названия как в базе?
                String countryCode = resultSet.getString("country_code");
                String operatorCode = resultSet.getString("operator_code");
                String phoneNumber = resultSet.getString("phone_number");
                PhoneType phoneType = PhoneType.valueOf(resultSet.getString("phone_type"));
                String comment = resultSet.getString("comment");
                int contactId = resultSet.getInt("contact_id");

                Phone phone = new Phone(phoneId, countryCode, operatorCode, phoneNumber, phoneType, comment, contactId);
                phones.add(phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("TELEPHONES:" + phones.toString());
        return phones;
    }

    @Override
    public Phone insert(Phone phone) {
        String sql = "INSERT INTO phone (country_code, operator_code, phone_number, phone_type, contact_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone.getCountryCode());
            ps.setString(2, phone.getOperatorCode());
            ps.setString(3, phone.getPhoneNumber());
            ps.setObject(4, phone.getPhoneType());
            ps.setInt(5, phone.getContactId());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                phone.setId(generatedKeys.getInt(1));
            } // чтобы получить id добавленного элемента
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return phone;
    }

    @Override
    public Phone update(Phone phone) {
        String sql = "UPDATE phone SET country_code=?, operator_code=?, phone_number=?, phone_type=?, comment=? WHERE contact_id = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone.getCountryCode());
            ps.setString(2, phone.getOperatorCode());
            ps.setString(3, phone.getPhoneNumber());
            ps.setObject(4, phone.getPhoneType()); // верно??
            ps.setString(5, phone.getComment());
            ps.setInt(6, phone.getContactId());
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return phone;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM phone WHERE phone_id=?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
