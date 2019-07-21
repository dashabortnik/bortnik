package com.itechart.bortnik.core.validation;

import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.Attachment;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Phone;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itechart.bortnik.core.database.DatabaseFieldNaming.*;

public class InputValidator {

    Map<String, Integer> allowedFieldSizes;
    public static final String EMAIL_PATTERN = "[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+";
    public static final String POSTCODE_PATTERN = "^\\d{5,6}(?:[-\\s]\\d{4})?$";
    public static final String COUNTRY_CODE_PATTERN = "^\\+?[0-9]+$";
    public static final String OPERATOR_CODE_PATTERN = "^\\(?[0-9]+\\)?$";
    public static final String PHONE_NUMBER_PATTERN = "^[0-9-]+$";

    public InputValidator() {
        allowedFieldSizes = new HashMap<>();
        allowedFieldSizes.put(DB_SURNAME, 30);
        allowedFieldSizes.put(DB_NAME, 30);
        allowedFieldSizes.put(DB_PATRONYMIC, 30);
        allowedFieldSizes.put(DB_NATIONALITY, 50);
        allowedFieldSizes.put(DB_WEBSITE, 50);
        allowedFieldSizes.put(DB_EMAIL, 50);
        allowedFieldSizes.put(DB_WORKPLACE, 50);
        allowedFieldSizes.put(DB_PHOTOLINK, 255);

        allowedFieldSizes.put(DB_COUNTRY, 50);
        allowedFieldSizes.put(DB_CITY, 50);
        allowedFieldSizes.put(DB_STREET, 100);
        allowedFieldSizes.put(DB_POSTCODE, 15);

        allowedFieldSizes.put(DB_COUNTRYCODE, 6);
        allowedFieldSizes.put(DB_OPERATORCODE, 5);
        allowedFieldSizes.put(DB_PHONENUMBER, 11);
        allowedFieldSizes.put(DB_COMMENT, 50);

        allowedFieldSizes.put(DB_ATTACHMENTNAME, 100);
        allowedFieldSizes.put(DB_ATTACHMENTLINK, 255);
    }

    Logger logger = LoggerFactory.getLogger(InputValidator.class);

    public List<String> validateObject(FullContactDTO entity) {
        //list of all found validation errors
        List<String> validationErrorList = new ArrayList<>();

        Contact contact = entity.getContact();
        Address address = contact.getAddress();
        //check if required fields are not empty
        validateRequiredField(contact.getSurname(), validationErrorList, DB_SURNAME);
        validateRequiredField(contact.getName(), validationErrorList, DB_NAME);
        validateRequiredField(contact.getBirthday().toString(), validationErrorList, DB_BIRTHDAY);
        validateRequiredField(contact.getEmail(), validationErrorList, DB_EMAIL);
        //check length of data in contact fields
        validateFieldLength(contact.getSurname(), validationErrorList, DB_SURNAME);
        validateFieldLength(contact.getName(), validationErrorList, DB_NAME);
        validateFieldLength(contact.getPatronymic(), validationErrorList, DB_PATRONYMIC);
        validateFieldLength(contact.getNationality(), validationErrorList, DB_NATIONALITY);
        validateFieldLength(contact.getWebsite(), validationErrorList, DB_WEBSITE);
        validateFieldLength(contact.getEmail(), validationErrorList, DB_EMAIL);
        validateFieldLength(contact.getWorkplace(), validationErrorList, DB_WORKPLACE);
        //check length of data in address fields
        validateFieldLength(address.getCountry(), validationErrorList, DB_COUNTRY);
        validateFieldLength(address.getCity(), validationErrorList, DB_CITY);
        validateFieldLength(address.getStreet(), validationErrorList, DB_STREET);
        validateFieldLength(address.getPostcode(), validationErrorList, DB_POSTCODE);
        //validate contact and address fields by regex patterns
        validateFieldByPattern(contact.getEmail(), validationErrorList, DB_EMAIL, EMAIL_PATTERN);
        validateFieldByPattern(address.getPostcode(), validationErrorList, DB_POSTCODE, POSTCODE_PATTERN);


        List<Phone> phoneList = entity.getPhoneList();
        for (Phone phone : phoneList) {
            //check length of data in phone fields
            validateFieldLength(phone.getCountryCode(), validationErrorList, DB_COUNTRYCODE);
            validateFieldLength(phone.getOperatorCode(), validationErrorList, DB_OPERATORCODE);
            validateFieldLength(phone.getPhoneNumber(), validationErrorList, DB_PHONENUMBER);
            validateFieldLength(phone.getComment(), validationErrorList, DB_COMMENT);

            //validate phone fields by regex patterns
            validateFieldByPattern(phone.getCountryCode(), validationErrorList, DB_COUNTRYCODE, COUNTRY_CODE_PATTERN);
            validateFieldByPattern(phone.getOperatorCode(), validationErrorList, DB_OPERATORCODE, OPERATOR_CODE_PATTERN);
            validateFieldByPattern(phone.getPhoneNumber(), validationErrorList, DB_PHONENUMBER, PHONE_NUMBER_PATTERN);
        }

        List<Attachment> attachmentList = entity.getAttachmentList();
        for (Attachment attachment : attachmentList) {
            //check length of data in attachment fields
            validateFieldLength(attachment.getName(), validationErrorList, DB_ATTACHMENTNAME);
            validateFieldLength(attachment.getLink(), validationErrorList, DB_ATTACHMENTLINK);
        }
        return validationErrorList;
    }

    private void validateRequiredField(String field, List<String> validationErrorList, String fieldName) {
        if (field == null || field.isEmpty()) {
            StringBuilder str = new StringBuilder("Required field is empty: ");
            str.append(fieldName.replace('_', ' '));
            validationErrorList.add(str.toString());
            logger.warn("On validation {} is empty.", fieldName);
        }
    }

    private void validateFieldLength(String field, List<String> validationErrorList, String fieldName) {
        int maxFieldLength = allowedFieldSizes.get(fieldName);
        if (field != null && field.length() > maxFieldLength) {
            StringBuilder str = new StringBuilder("Given field is too long: ");
            str.append(fieldName.replace('_', ' '));
            str.append(". Max length is ");
            str.append(maxFieldLength);
            str.append(" characters.");
            validationErrorList.add(str.toString());
            logger.warn("Field {} is too long. Max allowed length is {} characters.", fieldName, maxFieldLength);
        }
    }

    private void validateFieldByPattern(String field, List<String> validationErrorList, String fieldName, String pattern) {
        if (field != null && !"".equals(field) &&!field.matches(pattern)) {
            StringBuilder str = new StringBuilder("Illegal characters in field: ");
            str.append(fieldName.replace('_', ' '));
            validationErrorList.add(str.toString());
            logger.warn("Illegal characters in field {}.", fieldName);
        }

    }
}
