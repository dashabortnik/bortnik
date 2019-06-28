package com.itechart.bortnik.core.action;

import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteContactAction implements BaseAction {

    private ContactService contactService;

    public DeleteContactAction() {
        contactService = new ContactServiceImpl();
    }


    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("PATH:" + path);

        String data = request.getHeader("IdToDelete");
        System.out.println("READ DATA:" + data);

        String[] tokens = data.split(",");
        if (tokens != null && tokens.length != 0) {
            for (String token : tokens) {
                int id = Integer.parseInt(token);
                System.out.println("ID---"+id);
                contactService.remove(id);
                new ShowAllContactsAction().execute(request, response);
            }
        } else {
            System.out.println("Array of object ids to delete is empty. Could not delete the contact(s)");
        }
    }
}
