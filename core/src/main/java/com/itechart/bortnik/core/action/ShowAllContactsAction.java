package com.itechart.bortnik.core.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;

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

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        List<Contact> contactList = contactService.findAllContacts();
        try (PrintWriter out = response.getWriter()){

            mapper.writeValue(out, contactList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
