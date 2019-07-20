package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Gender;
import com.itechart.bortnik.core.domain.Marital;
import com.itechart.bortnik.core.domain.dto.PaginationContactListDTO;
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
                        String date = item.getString("UTF-8");
                        if(date!=null && !date.isEmpty()) {
                            birthdayMoreThan = dfShort.parse(date);
                            java.sql.Date sqlDate = new java.sql.Date(birthdayMoreThan.getTime());
                            birthdayMoreThan = sqlDate;
                        }
                        break;
                    }
                    case "birthdayLessThan": {
                        SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
                        String date = item.getString("UTF-8");
                        if(date!=null && !date.isEmpty()) {
                            birthdayLessThan = dfShort.parse(date);
                            java.sql.Date sqlDate = new java.sql.Date(birthdayLessThan.getTime());
                            birthdayLessThan = sqlDate;
                        }
                        break;
                    }
                    case "gender": {
                        String searchedGender = item.getString("UTF-8").trim();
                        if (!searchedGender.equals("none")) {
                            gender = Gender.valueOf(searchedGender);
                        }
                        break;
                    }
                    case "nationality": {
                        nationality = item.getString("UTF-8");
                        break;
                    }
                    case "marital": {
                        String searchedMarital = item.getString("UTF-8").trim();
                        if (!searchedMarital.equals("none")) {
                            maritalStatus = Marital.valueOf(searchedMarital);
                        }
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

        int page = 1;
        int pageSize = 10;

        if (request.getParameterMap().containsKey("page")) {
            page = Integer.valueOf(request.getParameter("page"));
        }

        if (request.getParameterMap().containsKey("pageSize")) {
            int newPageSize = Integer.valueOf(request.getParameter("pageSize"));
            switch (newPageSize){
                case 5:
                case 10:
                case 15:
                case 20:
                    pageSize = newPageSize;
                    break;
            }
        }

        if (searchContact != null) {

            int totalNumberOfContacts = contactService.countAllContactsByCriteria(searchContact);
            int maxPage = (int)Math.ceil((double)totalNumberOfContacts/pageSize);

            if (page<=0 || page > maxPage){
                page = 1;
            }

            int offset = (page-1)*pageSize; //limit = pageSize

            List<Contact> foundContacts = contactService.findContactsByCriteria(searchContact, offset, pageSize);
            System.out.println("FOUND---" + foundContacts.toString());
            PaginationContactListDTO paginationContactListDTO = new PaginationContactListDTO(foundContacts, page, pageSize, maxPage, searchContact);

            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);

            try (PrintWriter out = response.getWriter()){
                mapper.writeValue(out, paginationContactListDTO);
                logger.info("List of all contacts was sent to browser.");
            } catch (IOException e) {
                logger.error("Error with sending contacts to browser: ", e);
            }
        } else {
            logger.warn("Unable to perform search without search parameters.");
        }


    }
}
