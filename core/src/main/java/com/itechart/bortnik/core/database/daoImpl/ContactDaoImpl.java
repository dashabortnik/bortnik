package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.ContactDao;
import com.itechart.bortnik.core.database.DatabaseUtil;
import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Gender;
import com.itechart.bortnik.core.domain.Marital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ContactDaoImpl implements ContactDao {

    private static final String DB_CONTACTID = "contact_id";
    private static final String DB_SURNAME = "surname";
    private static final String DB_NAME = "name";
    private static final String DB_PATRONYMIC = "patronymic";
    private static final String DB_BIRTHDAY = "birthday";
    private static final String DB_GENDER = "gender";
    private static final String DB_NATIONALITY = "nationality";
    private static final String DB_MARITAL = "marital_status";
    private static final String DB_WEBSITE = "website";
    private static final String DB_EMAIL = "email";
    private static final String DB_WORKPLACE = "workplace";
    private static final String DB_PHOTOLINK = "photo_link";
    private static final String DB_ADDRESSID = "address_id";
    private static final String DB_COUNTRY = "country";
    private static final String DB_CITY = "city";
    private static final String DB_STREET = "street";
    private static final String DB_POSTCODE = "postcode";

    public ContactDaoImpl() {
    }

    // create the only instance of class and return it afterwards
    private static class Singleton {
        private static final ContactDao INSTANCE = new ContactDaoImpl();
    }

    public static ContactDao getInstance() {
        return Singleton.INSTANCE;
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(ContactDaoImpl.class);

    @Override
    public Contact readById(int id) {
        Contact contact;
        String sql = "SELECT * FROM contact LEFT JOIN address ON contact.contact_id=address.contact_id WHERE contact.contact_id=?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int contactId = resultSet.getInt(DB_CONTACTID);
                String surname = resultSet.getString(DB_SURNAME);
                String name = resultSet.getString(DB_NAME);
                String patronymic = resultSet.getString(DB_PATRONYMIC);
                Date birthday = resultSet.getDate(DB_BIRTHDAY);
                Gender gender = Gender.valueOf(resultSet.getString(DB_GENDER).trim());
                String nationality = resultSet.getString(DB_NATIONALITY);
                Marital maritalStatus = Marital.valueOf(resultSet.getString(DB_MARITAL).trim());
                String website = resultSet.getString(DB_WEBSITE);
                String email = resultSet.getString(DB_EMAIL);
                String workplace = resultSet.getString(DB_WORKPLACE);
                String photoLink = resultSet.getString(DB_PHOTOLINK);
                Address address = new Address(resultSet.getInt(DB_ADDRESSID), resultSet.getString(DB_COUNTRY),
                        resultSet.getString(DB_CITY), resultSet.getString(DB_STREET), resultSet.getString(DB_POSTCODE));
                contact = new Contact(contactId, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                        email, workplace, photoLink, address);
                logger.info("Contact was fetched from database successfully.");
                return contact;
            }

        } catch (SQLException e) {
            logger.error("Error with database query execution: ", e);
        }
        return null;
    }

    @Override
    public List<Contact> readByBirthday(Date today) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contact WHERE birthday=?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            System.out.println("Search for " + new java.sql.Date(today.getTime()).toString());
            ps.setObject(1, today);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int contactId = resultSet.getInt(DB_CONTACTID);
                String surname = resultSet.getString(DB_SURNAME);
                String name = resultSet.getString(DB_NAME);
                String patronymic = resultSet.getString(DB_PATRONYMIC);
                Date birthday = resultSet.getDate(DB_BIRTHDAY);
                Gender gender = Gender.valueOf(resultSet.getString(DB_GENDER).trim());
                String nationality = resultSet.getString(DB_NATIONALITY);
                Marital maritalStatus = Marital.valueOf(resultSet.getString(DB_MARITAL).trim());
                String website = resultSet.getString(DB_WEBSITE);
                String email = resultSet.getString(DB_EMAIL);
                String workplace = resultSet.getString(DB_WORKPLACE);
                String photoLink = resultSet.getString(DB_PHOTOLINK);
                Address address = null;
                contacts.add(new Contact(contactId, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                        email, workplace, photoLink, address));
            }
            System.out.println("TODAY BIRTHDAY: " + contacts.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    // collects all search parameters from searchedContact submitted by user, puts not empty parameters into map params
    private void collectSearchParams(Contact searchedContact, Map<String, Object> params) {
        String surname = searchedContact.getSurname();
        if (surname != null && surname.isEmpty()) {
            params.put(DB_SURNAME, surname);
        }
        String name = searchedContact.getName();
        if (name != null && name.isEmpty()) {
            params.put(DB_NAME, name);
        }
        String patronymic = searchedContact.getPatronymic();
        if (patronymic != null && patronymic.isEmpty()) {
            params.put(DB_PATRONYMIC, patronymic);
        }
        Date birthday = searchedContact.getBirthday(); // MORE OR LESS? ADD FLAG?
        if (birthday != null) {
            params.put(DB_BIRTHDAY, birthday);
        }
        Gender gender = searchedContact.getGender();
        if (gender != null) {
            params.put(DB_GENDER, gender);
        }
        String nationality = searchedContact.getNationality();
        if (nationality != null && nationality.isEmpty()) {
            params.put(DB_NATIONALITY, nationality);
        }
        Marital marital = searchedContact.getMaritalStatus();
        if (marital != null) {
            params.put(DB_MARITAL, marital);
        }
        Address address = searchedContact.getAddress();
        if (address != null) {
            String country = address.getCountry();
            if (country != null) {
                params.put(DB_COUNTRY, country);
            }
            String city = address.getCity();
            if (city != null) {
                params.put(DB_CITY, city);
            }
            String street = address.getStreet();
            if (street != null) {
                params.put(DB_STREET, street);
            }
            String postcode = address.getPostcode();
            if (postcode != null) {
                params.put(DB_POSTCODE, postcode);
            }
        }
    }

    @Override
    public List<Contact> readAll() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contact LEFT JOIN address ON contact.contact_id=address.contact_id";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             Statement st = connection.createStatement();
             ResultSet resultSet = st.executeQuery(sql)) {

            while (resultSet.next()) {
                int contactId = resultSet.getInt(DB_CONTACTID);
                String surname = resultSet.getString(DB_SURNAME);
                String name = resultSet.getString(DB_NAME);
                String patronymic = resultSet.getString(DB_PATRONYMIC);
                Date birthday = resultSet.getDate(DB_BIRTHDAY);
                Gender gender = Gender.valueOf(resultSet.getString(DB_GENDER).trim());
                String nationality = resultSet.getString(DB_NATIONALITY);
                Marital maritalStatus = Marital.valueOf(resultSet.getString(DB_MARITAL).trim());
                String website = resultSet.getString(DB_WEBSITE);
                String email = resultSet.getString(DB_EMAIL);
                String workplace = resultSet.getString(DB_WORKPLACE);
                String photoLink = resultSet.getString(DB_PHOTOLINK);
                Address address = new Address(resultSet.getInt(DB_ADDRESSID), resultSet.getString(DB_COUNTRY),
                        resultSet.getString(DB_CITY), resultSet.getString(DB_STREET), resultSet.getString(DB_POSTCODE));
                ;
                contacts.add(new Contact(contactId, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                        email, workplace, photoLink, address));
            }
        } catch (Exception e) {
            logger.error("Error with fetching contacts from database: ", e);
        }
        return contacts;
    }

    // поиск по нескольким критериям
    @Override
    public List<Contact> readByCriteria(Contact searchedContact) { // private methods

        List<Contact> contacts = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM contact LEFT JOIN address on contact.contact_id=address.contact_id ");

        // collect all parameters from the search into a map

        Map<String, Object> params = new HashMap<String, Object>();
        collectSearchParams(searchedContact, params);

        // put together dynamic query -- USE INSTR

        Set<String> keys = params.keySet();
        if (keys != null && !keys.isEmpty()) {
            sql.append(" WHERE");
            String andOp = "";
            for (String key : keys) {
                sql.append(andOp);
                sql.append(" ");
                sql.append(key);
                sql.append("=? ");
                andOp = " AND ";
            }
        }

        // execute query

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int position = 1;
            for (String key : keys) {
                ps.setObject(position, params.get(key)); // правильный ли порядок?
                position++;
            }

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                //Contact contact = assignValuesToContact(resultSet);
                //contacts.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;

    }

    @Override
    public Contact insert(Contact contact) {
        String sql = "INSERT INTO contact (surname, name, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAddress = "INSERT INTO address (country, city, street, postcode, contact_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psAddress = connection.prepareStatement(sqlAddress, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            logger.debug("Insert of new contact - begin transaction.");
            //query to insert new contact into database
            ps.setString(1, contact.getSurname());
            ps.setString(2, contact.getName());
            ps.setString(3, contact.getPatronymic());
            ps.setObject(4, contact.getBirthday());
            if (Gender.male.toString().equals(contact.getGender().toString().trim())) {
                ps.setObject(5, 1);
            } else {
                ps.setObject(5, 2);
            }
            ps.setString(6, contact.getNationality());
            if (Marital.single.toString().equals(contact.getMaritalStatus().toString().trim())) {
                ps.setObject(7, 1);
            } else if (Marital.married.toString().equals(contact.getMaritalStatus().toString().trim())) {
                ps.setObject(7, 2);
            } else {
                ps.setObject(7, 3);
            }
            ps.setString(8, contact.getWebsite());
            ps.setString(9, contact.getEmail());
            ps.setString(10, contact.getWorkplace());
            ps.setString(11, contact.getPhotoLink());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                contact.setId(generatedKeys.getInt(1));
            }
            //query to insert new address into database
            psAddress.setString(1, contact.getAddress().getCountry());
            psAddress.setString(2, contact.getAddress().getCity());
            psAddress.setString(3, contact.getAddress().getStreet());
            psAddress.setString(4, contact.getAddress().getPostcode());
            psAddress.setInt(5, contact.getId());
            psAddress.execute();
            ResultSet generatedKeysAddress = psAddress.getGeneratedKeys();
            if (generatedKeysAddress.next()) {
                contact.getAddress().setId(generatedKeys.getInt(1));
            }
            connection.commit();
            logger.debug("Insert of new contact - end transaction.");
        } catch (SQLException e) {
            logger.error("Error with inserting contact into database: ", e);
        }
        return contact;
    }

    @Override
    public Contact update(Contact contact) {
        String sql = "UPDATE contact SET surname=?, name=?, patronymic=?, birthday=?, gender=?, nationality=?, marital_status=?, website=?, email=?, workplace=?, photo_link=? WHERE contact_id = ?";
        String sqlAddress = "UPDATE address SET country=?, city=?, street=?, postcode=? WHERE contact_id = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             PreparedStatement psAddress = connection.prepareStatement(sqlAddress)) {
            connection.setAutoCommit(false);
            logger.debug("Update of contact - begin transaction.");
            //update the contact
            ps.setString(1, contact.getSurname());
            ps.setString(2, contact.getName());
            ps.setString(3, contact.getPatronymic());
            ps.setObject(4, contact.getBirthday());
            if (Gender.male.toString().equals(contact.getGender().toString().trim())) {
                ps.setObject(5, 1);
            } else {
                ps.setObject(5, 2);
            }
            ps.setString(6, contact.getNationality());
            if (Marital.single.toString().equals(contact.getMaritalStatus().toString().trim())) {
                ps.setObject(7, 1);
            } else if (Marital.married.toString().equals(contact.getMaritalStatus().toString().trim())) {
                ps.setObject(7, 2);
            } else {
                ps.setObject(7, 3);
            }
            ps.setString(8, contact.getWebsite());
            ps.setString(9, contact.getEmail());
            ps.setString(10, contact.getWorkplace());
            ps.setString(11, contact.getPhotoLink());
            ps.setInt(12, contact.getId());
            ps.execute();
            //update the address
            psAddress.setString(1, contact.getAddress().getCountry());
            psAddress.setString(2, contact.getAddress().getCity());
            psAddress.setString(3, contact.getAddress().getStreet());
            psAddress.setString(4, contact.getAddress().getPostcode());
            psAddress.setInt(5, contact.getId());
            psAddress.execute();
            connection.commit();
            logger.debug("Update of contact - end transaction.");
        } catch (SQLException e) {
            logger.error("Error with updating contact: ", e);
        }
        return contact;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM contact WHERE contact_id=?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
            logger.info("Delete of contact with id {} executed.", id);
        } catch (SQLException e) {
            logger.error("Error with deleting contact: ", e);
        }
    }
}