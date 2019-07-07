package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.DatabaseUtil;
import com.itechart.bortnik.core.database.PhoneDao;
import com.itechart.bortnik.core.domain.Phone;
import com.itechart.bortnik.core.domain.PhoneType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneDaoImpl implements PhoneDao {

    public PhoneDaoImpl() {
    }

    // create the only instance of class and return it afterwards
    private static class Singleton {
        private static final PhoneDaoImpl INSTANCE = new PhoneDaoImpl();
    }

    public static PhoneDaoImpl getInstance() {
        return Singleton.INSTANCE;
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(PhoneDaoImpl.class);

    @Override
    public List<Phone> readAllById(int id) {
        List<Phone> phones = new ArrayList<>();
        String sql = "SELECT * FROM phone WHERE contact_id = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int phoneId = resultSet.getInt("phone_id");
                String countryCode = resultSet.getString("country_code");
                String operatorCode = resultSet.getString("operator_code");
                String phoneNumber = resultSet.getString("phone_number");
                PhoneType phoneType = PhoneType.valueOf(resultSet.getString("phone_type"));
                String comment = resultSet.getString("comment");
                int contactId = resultSet.getInt("contact_id");
                //create Phone with data retrieved from database
                Phone phone = new Phone(phoneId, countryCode, operatorCode, phoneNumber, phoneType, comment, contactId);
                phones.add(phone);
            }
        } catch (Exception e) {
            logger.error("Error with fetching phones from database: ", e);
        }
        logger.info("Phones for contact {} were fetched successfully.", id);
        return phones;
    }

    @Override
    public Phone insert(Phone phone) {
        String sql = "INSERT INTO phone (country_code, operator_code, phone_number, phone_type, comment, contact_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, phone.getCountryCode());
            ps.setString(2, phone.getOperatorCode());
            ps.setString(3, phone.getPhoneNumber());
            if (PhoneType.home.toString().equals(phone.getPhoneType().toString().trim())) {
                ps.setObject(4, 1);
            } else {
                ps.setObject(4, 2);
            }
            ps.setString(5, phone.getComment());
            ps.setInt(6, phone.getContactId());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                phone.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            logger.error("Error with inserting phone into database: ", e);
        }
        logger.info("Phone for contact {} was inserted successfully.", phone.getId());
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
            ps.setObject(4, phone.getPhoneType());
            ps.setString(5, phone.getComment());
            ps.setInt(6, phone.getContactId());
            ps.execute();
            logger.info("Phone was updated successfully.");
        } catch (SQLException e) {
            logger.error("Error with updating phone: ", e);
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
            logger.info("Phone with id {} was deleted successfully.", id);
        } catch (SQLException e) {
            logger.error("Error with deleting phone: ", e);
        }
    }

}
