package com.itechart.bortnik.web.action;

import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteContactAction implements BaseAction {

    private ContactService contactService;

    public DeleteContactAction() {
        contactService = new ContactServiceImpl();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(DeleteContactAction.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get ids of contacts that should be deleted
        String data = request.getHeader("IdToDelete");
        logger.debug("Contacts with id: {} should be deleted.", data);

        String[] tokens = data.split(",");
        if (tokens != null && tokens.length != 0) {
            for (String token : tokens) {
                int id = Integer.parseInt(token);
                contactService.remove(id);
                logger.info("Contact with id: {} was deleted.", id);
            }
            new ShowAllContactsAction().execute(request, response);
        } else {
            logger.warn("Array of object ids to delete is empty. Could not delete the contact(s)");
        }
    }
}
