package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Contact;
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

        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        List<Contact> contactList = contactService.findAllContacts();
        try (PrintWriter out = response.getWriter()){
            mapper.writeValue(out, contactList);
            logger.info("List of all contacts was sent to browser.");
        } catch (IOException e) {
            logger.error("Error with sending contacts to browser: ", e);
        }
    }
}
