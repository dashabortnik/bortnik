package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;
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

public class GetContactByIdAction implements BaseAction{

    private ContactService contactService;

    public GetContactByIdAction() {
        contactService = new ContactServiceImpl();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(GetContactByIdAction.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        //Example String: empty-contacts-number
        String urlPart = (request.getRequestURI().split("/"))[4];
        int searchedId = Integer.parseInt(urlPart);
        logger.info("Contact with id: {} was requested.", searchedId);

        FullContactDTO fullContact = contactService.findContactById(searchedId);
        logger.debug("Retrieved contact: {}", fullContact);

        try (PrintWriter out = response.getWriter()){
            mapper.writeValue(out, fullContact);
            logger.info("Requested contact with id: {} was sent to browser.", searchedId);
        } catch (IOException e) {
            logger.error("Error with contact output to browser: ", e);
        }
    }
}
