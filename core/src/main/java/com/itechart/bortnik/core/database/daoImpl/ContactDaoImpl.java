package com.itechart.bortnik.core.database.daoImpl;

import com.itechart.bortnik.core.database.ContactDao;
import com.itechart.bortnik.core.database.DatabaseUtil;
import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Gender;
import com.itechart.bortnik.core.domain.Marital;
import com.itechart.bortnik.core.domain.dto.SearchContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private static final String DB_ATTACHMENTLINK = "attachment_link";
    private static final String DB_BIRTHDAY_MORE_THAN = "birthdayMoreThan";
    private static final String DB_BIRTHDAY_LESS_THAN = "birthdayLessThan";

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
    public List<Contact> readByBirthday() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contact WHERE MONTH(birthday) = MONTH(NOW()) AND DAY(birthday) = DAY(NOW())";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
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
            logger.debug("List of contacts with birthday today: " + contacts.toString());
            return contacts;
        } catch (SQLException e) {
            logger.error("Error with reading contacts with birthday today: ", e);
        }
        return contacts;
    }

    // collects all search parameters from searchedContact submitted by user, puts not empty parameters into map params
    private Map<String, Object> collectSearchParams(SearchContactDTO searchedContact, Map<String, Object> params) {
        String surname = searchedContact.getSurname();
        if (surname != null && !surname.isEmpty()) {
            params.put(DB_SURNAME, surname);
        }
        String name = searchedContact.getName();
        if (name != null && !name.isEmpty()) {
            params.put(DB_NAME, name);
        }
        String patronymic = searchedContact.getPatronymic();
        if (patronymic != null && !patronymic.isEmpty()) {
            params.put(DB_PATRONYMIC, patronymic);
        }
        Date birthdayMoreThan = searchedContact.getBirthdayMoreThan();
        if (birthdayMoreThan != null) {
            params.put(DB_BIRTHDAY_MORE_THAN, birthdayMoreThan);
        }
        Date birthdayLessThan = searchedContact.getBirthdayLessThan();
        if (birthdayLessThan != null) {
            params.put(DB_BIRTHDAY_LESS_THAN, birthdayLessThan);
        }
        Gender gender = searchedContact.getGender();
        if (gender != null) {
            params.put(DB_GENDER, gender);
        }
        String nationality = searchedContact.getNationality();
        if (nationality != null && !nationality.isEmpty()) {
            params.put(DB_NATIONALITY, nationality);
        }
        Marital marital = searchedContact.getMaritalStatus();
        if (marital != null) {
            params.put(DB_MARITAL, marital);
        }
        Address address = searchedContact.getAddress();
        if (address != null) {
            String country = address.getCountry();
            if (country != null && !country.isEmpty()) {
                params.put(DB_COUNTRY, country);
            }
            String city = address.getCity();
            if (city != null && !city.isEmpty()) {
                params.put(DB_CITY, city);
            }
            String street = address.getStreet();
            if (street != null && !street.isEmpty()) {
                params.put(DB_STREET, street);
            }
            String postcode = address.getPostcode();
            if (postcode != null && !postcode.isEmpty()) {
                params.put(DB_POSTCODE, postcode);
            }
        }
        logger.debug("Search parameters map: " + Arrays.asList(params));
        logger.debug("Search parameters extracted");
        return params;
    }

    @Override
    public List<Contact> readAll(int offset, int limit) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contact LEFT JOIN address ON contact.contact_id=address.contact_id LIMIT ?, ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
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
                ;
                contacts.add(new Contact(contactId, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                        email, workplace, photoLink, address));
            }
        } catch (Exception e) {
            logger.error("Error with fetching contacts from database: ", e);
        }
        return contacts;
    }

    // search by several criteria
    @Override
    public List<Contact> readByCriteria(SearchContactDTO searchedContact, int offset, int limit) { // private methods

        List<Contact> contacts = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM contact LEFT JOIN address on contact.contact_id=address.contact_id ");

        // collect all parameters from the search into a map
        Map<String, Object> params = new HashMap<>();
        collectSearchParams(searchedContact, params);
        logger.debug("Search parameters map from method: " + Arrays.asList(params));

        // put together dynamic query
        Map<Integer, String> orderedParams = new HashMap<>();

        Set<String> keys = params.keySet();
        int paramCounter = 0;
        if (keys != null && !keys.isEmpty()) {
            sql.append(" WHERE ");
            String andOp = "";
            for (String key : keys) {
                sql.append(andOp);
                if (key.equals("birthdayMoreThan")) {
                    sql.append("birthday>=?");
                } else if (key.equals("birthdayLessThan")) {
                    sql.append("birthday<=?");
                } else {
                    sql.append("INSTR(");
                    sql.append(key);
                    sql.append(", ?)=1");
                }
                andOp = " AND ";
                paramCounter++;
                orderedParams.put(new Integer(paramCounter), params.get(key).toString());
            }
        }

        //ADD SECOND QUERY
        sql.append(" LIMIT ?,?");
        logger.debug("Constructed query: {}", sql.toString());
        System.out.println("NEW MAP: " + Arrays.asList(orderedParams));

        // execute query
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            //fill in prepared statement with values
            int i;
            for (i = 1; i <= orderedParams.size(); i++) {
                ps.setString(i, orderedParams.get(i));
            }
            int numOfArgs = paramCounter + 2;
            ps.setInt(numOfArgs - 1, offset);
            ps.setInt(numOfArgs, limit);
            System.out.println(ps);

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
                contacts.add(new Contact(contactId, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                        email, workplace, photoLink, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("FOUND CONTACTS: " + contacts.toString());
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
//        String sql = "UPDATE contact SET surname=?, name=?, patronymic=?, birthday=?, gender=?, nationality=?, marital_status=?, website=?, email=?, workplace=?, photo_link=? WHERE contact_id = ?";
//        String sqlAddress = "UPDATE address SET country=?, city=?, street=?, postcode=? WHERE contact_id = ?";
//        String deleteSql = "DELETE FROM contact WHERE contact_id = ?";

        String sql = "INSERT INTO contact (surname, name, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAddress = "INSERT INTO address (country, city, street, postcode, contact_id) VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;
//        PreparedStatement psDelete = null;
        PreparedStatement ps = null;
        PreparedStatement psAddress = null;
        try {
            connection = DatabaseUtil.getDataSource().getConnection();
//            psDelete = connection.prepareStatement(deleteSql);
            ps = connection.prepareStatement(sql);
            psAddress = connection.prepareStatement(sqlAddress);
            connection.setAutoCommit(false);
            logger.debug("Update of contact - begin transaction. {}", contact.getId());

//            psDelete.setInt(1, contact.getId());
//            ps.execute();
            delete(contact.getId());
            logger.info("Delete of contact with id {} executed.", contact.getId());
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
            return contact;
        } catch (SQLException e) {
            try {
                logger.error("Error with updating contact: ", e);
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Error with updating contact: ", ex);
            }

        } finally {
//            if (psDelete != null) {
//                try {
//                    psDelete.close();
//                } catch (SQLException e) {
//                    logger.error("Error with closing delete prepared statement: ", e);
//                }
//            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("Error with closing insert contact prepared statement: ", e);
                }
            }
            if (psAddress != null) {
                try {
                    psAddress.close();
                } catch (SQLException e) {
                    logger.error("Error with closing insert address prepared statement: ", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error with closing connection: ", e);
                }
            }
        }
        return null;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM contact WHERE contact_id=?";
        String sqlGetPhoto = "SELECT photo_link FROM contact WHERE contact_id = ?";
        String sqlGetAttachments = "SELECT attachment_link FROM attachment WHERE contact_id = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             PreparedStatement psgetPhoto = connection.prepareStatement(sqlGetPhoto);
             PreparedStatement psGetAttachments = connection.prepareStatement(sqlGetAttachments)) {
            //retrieve photo link and delete corresponding photo
            psgetPhoto.setInt(1, id);
            ResultSet resultSetPhoto = psgetPhoto.executeQuery();
            while (resultSetPhoto.next()) {
                String photoLink = resultSetPhoto.getString(DB_PHOTOLINK);
                File file = new File(photoLink);
                if (file.delete()) { //if deleted, returns true
                    logger.info("Image of contact {} was deleted.", id);
                } else {
                    logger.warn("Deleting image of contact {} failed.", id);
                }
            }
            //retrieve attachment links and delete corresponding files
            psGetAttachments.setInt(1, id);
            ResultSet resultSetAttachments = psGetAttachments.executeQuery();
            while (resultSetAttachments.next()) {
                String attachmentLink = resultSetAttachments.getString(DB_ATTACHMENTLINK);
                File file = new File(attachmentLink);
                if (file.delete()) { //if deleted, returns true
                    logger.info("Attachment of contact {} was deleted.", id);
                } else {
                    logger.warn("Deleting attachment of contact {} failed.", id);
                }
            }
            //delete requested contact
            ps.setInt(1, id);
            ps.execute();
            logger.info("Delete of contact with id {} executed.", id);
        } catch (SQLException e) {
            logger.error("Error with deleting contact: ", e);
        }
    }

    @Override
    public int countContacts() {
        int totalNumberOfContacts = 0;
        String sql = "SELECT COUNT(*) AS totalNumber FROM contact";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            while (resultSet.next()) {
                totalNumberOfContacts = resultSet.getInt("totalNumber");
            }
            return totalNumberOfContacts;
        } catch (SQLException e) {
            logger.error("Error with reading contacts with birthday today: ", e);
        }
        return totalNumberOfContacts;
    }

    @Override
    public String readEmailById(int id) {
        String sql = "SELECT email FROM contact WHERE contact_id=?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String email = resultSet.getString(DB_EMAIL);
                logger.info("Email from contact {} was fetched from database successfully.", id);
                return email;
            }

        } catch (SQLException e) {
            logger.error("Error with database query execution: ", e);
        }
        return null;
    }
}