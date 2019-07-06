package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.*;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;
import com.itechart.bortnik.core.service.AttachmentService;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.PhoneService;
import com.itechart.bortnik.core.service.serviceImpl.AttachmentServiceImpl;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import com.itechart.bortnik.core.service.serviceImpl.PhoneServiceImpl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CreateContactAction implements BaseAction {

    private ContactService contactService;
    private PhoneService phoneService;
    private AttachmentService attachmentService;

    public CreateContactAction() {
        contactService = new ContactServiceImpl();
        phoneService = new PhoneServiceImpl();
        attachmentService = new AttachmentServiceImpl();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(CreateContactAction.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create empty objects and variables for parsing
        Contact receivedContact = null;
        String surname = null;
        String name = null;
        String patronymic = null;
        Date birthday = null;
        Gender gender = null;
        String nationality = null;
        Marital maritalStatus = null;
        String website = null;
        String email = null;
        String workplace = null;
        String country = null;
        String city = null;
        String street = null;
        String postcode = null;
        String photoLink = null;
        List<Phone> phones = new ArrayList<Phone>();
        String countryCode = null;
        String operatorCode = null;
        String phoneNumber = null;
        PhoneType phoneType = null;
        String comment = null;
        int phoneCounter = 1;
        List<Attachment> attachments = new ArrayList<Attachment>();
        String attachmentName = null;
        String attachmentLink = null;
        String submittedFileName = null;

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        List<FileItem> items = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            items = upload.parseRequest(request);

            cycle:
            for (FileItem item : items) {

                String fieldName = item.getFieldName();

                if (fieldName.matches("phone.*")) {
                    //flag that shows if the same fieldName should be checked again
                    int checkAgain = 1;
                    //parse fieldName to find ordinal number of the phone and item name
                    //example: phone.1.countryCode
                    String[] fieldNameParts = fieldName.split("\\.");
                    int itemNumber = Integer.parseInt(fieldNameParts[1]);
                    String itemName = fieldNameParts[2];
                    //parse values depending on item name, all fields are required, only comment can be omitted
                    //new phone is created either after a comment was reached, or when the itemNumber has changed
                    while (checkAgain == 1) {
                        if (itemNumber == phoneCounter) {
                            switch (itemName) {
                                case "countryCode":
                                    countryCode = item.getString();
                                    break;
                                case "operatorCode":
                                    operatorCode = item.getString();
                                    break;
                                case "phoneNumber":
                                    phoneNumber = item.getString();
                                    break;
                                case "phoneType":
                                    phoneType = PhoneType.valueOf(item.getString().trim());
                                    break;
                                case "comment":
                                    comment = item.getString();
                                    if (countryCode != null && operatorCode != null && phoneNumber != null && phoneType != null) {
                                        Phone phone = new Phone(0, countryCode, operatorCode,
                                                phoneNumber, phoneType, comment, 0);
                                        phones.add(phone);
                                        countryCode = null;
                                        operatorCode = null;
                                        phoneNumber = null;
                                        phoneType = null;
                                        comment = null;
                                    }
                                    break;
                            }
                            checkAgain = 0;
                            continue cycle;
                        } else {
                            if (countryCode != null && operatorCode != null && phoneNumber != null && phoneType != null) {
                                Phone phone = new Phone(0, countryCode, operatorCode,
                                        phoneNumber, phoneType, comment, 0);
                                phones.add(phone);
                                countryCode = null;
                                operatorCode = null;
                                phoneNumber = null;
                                phoneType = null;
                                comment = null;
                            }

                            //when the itemNumber has changed, counter+1 and the field should be checked again
                            phoneCounter++;
                            checkAgain = 1;
                        }
                    }

                } else if (fieldName.matches("attachment.*")) {

                    //parse fieldName to find ordinal number of the attachment and item name
                    //example: attachment.1.attachmentName
                    String[] fieldNameParts = fieldName.split("\\.");
                    int itemNumber = Integer.parseInt(fieldNameParts[1]);
                    String itemName = fieldNameParts[2];
                    //parse values depending on item name, all fields are required
                    //new attachment is created when link is reached
                    switch (itemName) {
                        case "attachmentName":
                            attachmentName = item.getString();
                            break;
                        case "submittedFileName":
                            String fakeName = item.getString();
                            //String [] nameParts = fakeName.split("/");

                            if (fakeName.contains("fakepath")) {
                                // update the file-path text using case-insensitive regex
                                submittedFileName = fakeName.replace("C:\\fakepath\\", "");
                            } else {
                                submittedFileName = fakeName;
                            }
                            System.out.println("SUB FILENAME---" + submittedFileName);
                            break;
                        case "attachmentLink":
                            String path = FileSystems.getDefault().getPath("").toAbsolutePath() + File.separator
                                    + "file" + File.separator + submittedFileName;
                            File uploadedFile = new File(path);
                            item.write(uploadedFile);
                            System.out.println("File path: " + uploadedFile.getAbsolutePath());
                            attachmentLink = uploadedFile.getAbsolutePath();

                            //create attachment and add it to the list
                            if (attachmentName != null && !attachmentName.equals("")&& attachmentLink != null &&
                                    !attachmentLink.equals("")) {
                                Attachment attachment = new Attachment(0, attachmentName, attachmentLink,
                                        new java.sql.Date(new Date().getTime()), 0);
                                attachments.add(attachment);
                                attachmentName = null;
                                attachmentLink = null;
                                submittedFileName = null;
                            } else {
                                System.out.println("Problem with new attachment. Name or link are empty.");
                            }
                            break;
                    }
                } else if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        case "surname": {
                            surname = item.getString();
                            break;
                        }
                        case "name": {
                            name = item.getString();
                            break;
                        }
                        case "patronymic": {
                            patronymic = item.getString();
                            break;
                        }
                        case "birthday": {
                            SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
                            birthday = dfShort.parse(item.getString());
                            java.sql.Date sqlDate = new java.sql.Date(birthday.getTime());
                            birthday = sqlDate;
                            break;
                        }
                        case "gender": {
                            gender = Gender.valueOf(item.getString().trim());
                            break;
                        }
                        case "nationality": {
                            nationality = item.getString();
                            break;
                        }
                        case "marital": {
                            maritalStatus = Marital.valueOf(item.getString().trim());
                            break;
                        }
                        case "website": {
                            website = item.getString();
                            break;
                        }
                        case "email": {
                            email = item.getString();
                            break;
                        }
                        case "workplace": {
                            workplace = item.getString();
                            break;
                        }
                        case "country": {
                            country = item.getString();
                            break;
                        }
                        case "city": {
                            city = item.getString();
                            break;
                        }
                        case "street": {
                            street = item.getString();
                            break;
                        }
                        case "postcode": {
                            postcode = item.getString();
                            break;
                        }
                        default: {
                            continue cycle;
                        }
                    }
                } else {
                    String path = FileSystems.getDefault().getPath("").toAbsolutePath() + File.separator + "img"
                            + File.separator + ThreadLocalRandom.current().nextInt(1, 2147483646 + 1) + ".jpg";
                    File uploadedFile = new File(path);
                    item.write(uploadedFile);
                    System.out.println("Image path: " + uploadedFile.getAbsolutePath());
                    photoLink = uploadedFile.getAbsolutePath();
                }
            }
            receivedContact = new Contact(0, surname, name, patronymic, birthday, gender, nationality, maritalStatus, website,
                    email, workplace, photoLink, new Address(0, country, city, street, postcode));

        } catch (
                FileUploadException ex) {
            System.out.println("Error With File Parsing - " + ex.getMessage());
        } catch (
                ParseException e) {
            e.printStackTrace();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        //create DTO object with contact, list of phones and list of attachments
        FullContactDTO fullContactDTO = new FullContactDTO(receivedContact, phones, attachments);
        System.out.println(fullContactDTO.toString());

        if (receivedContact != null) {
            Contact fullContact = contactService.save(receivedContact);

            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            try (PrintWriter out = response.getWriter()) {
                mapper.writeValue(out, fullContact);            //CHANGE!!!
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("Unable to add new contact to DB. The received contact is empty.");
        }
    }
}
