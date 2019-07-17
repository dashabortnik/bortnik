package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Gender;
import com.itechart.bortnik.core.domain.Marital;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;
import com.itechart.bortnik.core.domain.dto.SearchContactDTO;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchForContactsAction implements BaseAction {

    private ContactService contactService;

    public SearchForContactsAction() {
        contactService = new ContactServiceImpl();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(SearchForContactsAction.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //create empty objects and variables for parsing
        SearchContactDTO searchContact = null;
        String surname = null;
        String name = null;
        String patronymic = null;
        Date birthdayMoreThan = null;
        Date birthdayLessThan = null;
        Gender gender = null;
        String nationality = null;
        Marital maritalStatus = null;
        String country = null;
        String city = null;
        String street = null;
        String postcode = null;

        List<FileItem> items = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            items = upload.parseRequest(request);

            cycle:
            for (FileItem item : items) {

                String fieldName = item.getFieldName();

                switch (fieldName) {
                    case "surname": {
                        surname = item.getString("UTF-8");
                        break;
                    }
                    case "name": {
                        name = item.getString("UTF-8");
                        break;
                    }
                    case "patronymic": {
                        patronymic = item.getString("UTF-8");
                        break;
                    }
                    case "birthdayMoreThan": {
                        SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
                        birthdayMoreThan = dfShort.parse(item.getString("UTF-8"));
                        java.sql.Date sqlDate = new java.sql.Date(birthdayMoreThan.getTime());
                        birthdayMoreThan = sqlDate;
                        break;
                    }
                    case "birthdayLessThan": {
                        SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
                        birthdayLessThan = dfShort.parse(item.getString("UTF-8"));
                        java.sql.Date sqlDate = new java.sql.Date(birthdayLessThan.getTime());
                        birthdayLessThan = sqlDate;
                        break;
                    }
                    case "gender": {
                        gender = Gender.valueOf(item.getString("UTF-8").trim());
                        break;
                    }
                    case "nationality": {
                        nationality = item.getString("UTF-8");
                        break;
                    }
                    case "marital": {
                        maritalStatus = Marital.valueOf(item.getString("UTF-8").trim());
                        break;
                    }
                    case "country": {
                        country = item.getString("UTF-8");
                        break;
                    }
                    case "city": {
                        city = item.getString("UTF-8");
                        break;
                    }
                    case "street": {
                        street = item.getString("UTF-8");
                        break;
                    }
                    case "postcode": {
                        postcode = item.getString("UTF-8");
                        break;
                    }
                    default: {
                        continue cycle;
                    }
                }

            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        searchContact = new SearchContactDTO(surname, name, patronymic, birthdayMoreThan, birthdayLessThan,
                gender, nationality, maritalStatus, new Address(0, country, city, street, postcode));
        logger.debug("Contact with search data: {}", searchContact);

        if (searchContact != null) {
            List<Contact> foundContacts = contactService.findContactsByCriteria(searchContact);
            System.out.println("FOUND---" + foundContacts.toString());
            //write the created contact into response
//            response.setHeader("Content-Type", "application/json; charset=UTF-8");
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setDateFormat(df);
//            try (PrintWriter out = response.getWriter()) {
//                mapper.writeValue(out, fullContact);
//                logger.info("Created contact sent to browser.");
//            } catch (IOException e) {
//                logger.error("Error with reading or writing the file: ", e);
//            }
        } else {
            logger.warn("Unable to perform search without search parameters.");
        }


    }
}
