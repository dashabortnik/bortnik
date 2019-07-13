package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.dto.PaginationContactList;
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

        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        int totalNumberOfContacts = contactService.countAllContacts();
        int maxPage = (int)Math.ceil((double)totalNumberOfContacts/pageSize);

        if (page<=0 || page > maxPage){
            page = 1;
        }

        int offset = (page-1)*pageSize; //limit = pageSize
        List<Contact> contactList = contactService.findAllContacts(offset, pageSize);

        PaginationContactList paginationContactList = new PaginationContactList(contactList, page, pageSize, maxPage);

        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        try (PrintWriter out = response.getWriter()){
            mapper.writeValue(out, paginationContactList);
            logger.info("List of all contacts was sent to browser.");
        } catch (IOException e) {
            logger.error("Error with sending contacts to browser: ", e);
        }
    }
}
