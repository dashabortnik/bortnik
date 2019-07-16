package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.bortnik.core.domain.dto.EmailListDTO;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GetEmailByIdAction implements BaseAction {

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(DeleteContactAction.class);

    private ContactService contactService;
    public GetEmailByIdAction() {
        contactService = new ContactServiceImpl();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get ids of contacts user needs emails for
        String data = request.getHeader("IdForEmails");
        logger.info("User requested emails for contacts with id: {}.", data);

        List<String> emailList = new ArrayList<>();

        String[] tokens = data.split(",");
        if (tokens != null && tokens.length != 0) {
            for (String token : tokens) {
                int id = Integer.parseInt(token);
                String email = contactService.findEmailById(id);
                if (email!=null){
                    emailList.add(email);
                    logger.info("Email from contact {} was retrieved.", id);
                }
            }
        } else {
            logger.warn("Array of contact ids is empty. Could not get the emails.");
        }

        if(!emailList.isEmpty()){

            EmailListDTO emailListDTO = new EmailListDTO(emailList);
            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();

            try (PrintWriter out = response.getWriter()) {
                mapper.writeValue(out, emailListDTO);
                logger.info("Retrieved email addresses were sent to browser.");
            } catch (IOException e) {
                logger.error("Error with email address output to browser: ", e);
            }
        } else {
            logger.warn("Email list is empty.");
            //display error page
        }
    }
}
