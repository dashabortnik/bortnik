package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.dto.PaginationContactListDTO;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class ShowAllContactsAction implements BaseAction {

    private ContactService contactService;

    public ShowAllContactsAction() {
        contactService = new ContactServiceImpl();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(ShowAllContactsAction.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        if (request.getParameterMap().containsKey("page")) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        if (request.getParameterMap().containsKey("pageSize")) {
            int newPageSize = Integer.parseInt(request.getParameter("pageSize"));
            switch (newPageSize){
                case 5:
                case 10:
                case 15:
                case 20:
                pageSize = newPageSize;
                break;
            }
        }

        int totalNumberOfContacts = contactService.countAllContacts();
        int maxPage = (int)Math.ceil((double)totalNumberOfContacts/pageSize);

        if (page<=0 || page > maxPage){
            page = 1;
        }

        int offset = (page-1)*pageSize; //limit = pageSize
        List<Contact> contactList = contactService.findAllContacts(offset, pageSize);

        PaginationContactListDTO paginationContactListDTO = new PaginationContactListDTO(contactList, page, pageSize, maxPage, totalNumberOfContacts,null);

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
    }
}
