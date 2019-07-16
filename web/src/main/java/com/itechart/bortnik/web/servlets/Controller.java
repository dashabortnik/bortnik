package com.itechart.bortnik.web.servlets;

import com.itechart.bortnik.web.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    public static final String GET_ALL_CONTACTS = "GETALLCONTACTS";
    public static final String CREATE_CONTACT = "CREATECONTACT";
    public static final String GET_CONTACT_BY_ID = "GETCONTACTBYID";
    public static final String UPDATE_CONTACT = "UPDATECONTACT";
    public static final String GET_IMAGE_FOR_CONTACT = "GETIMAGEFORCONTACT";
    public static final String GET_FILE = "GETFILE";
    public static final String EDIT_CONTACT_FORM = "EDITCONTACTFORM";
    public static final String DELETE_CONTACT = "DELETECONTACT";
    public static final String SEND_EMAILS = "SENDEMAILS";
    public static final String GET_EMAIL_ADDRESSES = "GETEMAILADDRESSES";
    public static final String GET_EMAIL_TEMPLATE = "GETEMAILTEMPLATE";

    private Map<String, String> actions;

    public Controller() {
        actions = new HashMap<>();
        actions.put("POST_\\/", CREATE_CONTACT);
        actions.put("GET_\\/\\d+\\/edit-form\\/*", EDIT_CONTACT_FORM);
        actions.put("GET_\\/\\d+\\/*", GET_CONTACT_BY_ID);
        actions.put("POST_\\/\\d+", UPDATE_CONTACT);
        actions.put("GET_\\/image", GET_IMAGE_FOR_CONTACT);
        actions.put("GET_\\/file", GET_FILE);
        actions.put("DELETE_\\/*.*", DELETE_CONTACT);
        actions.put("DELETE_\\/\\d+", DELETE_CONTACT);
        actions.put("DELETE_\\/\\d+.*", DELETE_CONTACT);
        actions.put("GET_\\/email", GET_EMAIL_ADDRESSES );
        actions.put("GET_\\/template", GET_EMAIL_TEMPLATE );
        actions.put("POST_\\/email", SEND_EMAILS );
        actions.put("GET_\\/*", GET_ALL_CONTACTS);
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(Controller.class);

    void processString(String parsedUrl, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = "";
        for (String key : actions.keySet()) {
            if (parsedUrl != null && !parsedUrl.isEmpty() && parsedUrl.matches(key)) {
                action = actions.get(key);
                break;
            }
        }
        logger.debug("ACTION: {}", action);

        switch (action) {
            case "":
            case GET_ALL_CONTACTS:
                logger.info("ShowAllContactsAction is invoked.");
                new ShowAllContactsAction().execute(request, response);
                break;
            case CREATE_CONTACT:
                logger.info("CreateContactAction is invoked.");
                new CreateContactAction().execute(request, response);
                break;
            case GET_CONTACT_BY_ID:
            case EDIT_CONTACT_FORM:
                logger.info("GetContactByIdAction is invoked.");
                new GetContactByIdAction().execute(request, response);
                break;
            case GET_IMAGE_FOR_CONTACT:
                logger.info("GetImageForContactAction is invoked.");
                new GetImageForContactAction().execute(request, response);
                break;
            case GET_FILE:
                logger.info("GetFileAction is invoked.");
                new GetFileAction().execute(request, response);
                break;
            case UPDATE_CONTACT:
                logger.info("UpdateContactAction is invoked.");
                new UpdateContactAction().execute(request, response);
                break;
            case DELETE_CONTACT:
                logger.info("DeleteContactAction is invoked.");
                new DeleteContactAction().execute(request, response);
                break;
            case GET_EMAIL_ADDRESSES:
                logger.info("GetEmailByIdAction is invoked.");
                new  GetEmailByIdAction().execute(request, response);
                break;
            case GET_EMAIL_TEMPLATE:
                logger.info("GetEmailTemplateAction is invoked.");
                new  GetEmailTemplateAction().execute(request, response);
                break;
            case SEND_EMAILS:
                logger.info("SendEmailAction is invoked.");
                new  SendEmailAction().execute(request, response);
                break;
            default:
                logger.info("ShowAllContactsAction is invoked by default.");
                new ShowAllContactsAction().execute(request, response);
                break;
        }
    }
}
