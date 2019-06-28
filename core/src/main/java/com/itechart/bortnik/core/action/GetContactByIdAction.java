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

public class GetContactByIdAction implements BaseAction{

    private ContactService contactService;

    public GetContactByIdAction() {
        contactService = new ContactServiceImpl();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        //empty-contacts-number
        String urlPart = (request.getRequestURI().split("/"))[2];
        int searchedId = Integer.parseInt(urlPart);
        System.out.println(searchedId);
        Contact contact = contactService.findContactById(searchedId);

        try (PrintWriter out = response.getWriter()){
            mapper.writeValue(out, contact);
            System.out.println("Contact fetched successfully");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
