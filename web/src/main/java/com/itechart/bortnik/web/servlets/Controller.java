package com.itechart.bortnik.web.servlets;

import com.itechart.bortnik.web.action.*;

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
    public static final String GET_ALL_EMAILS = "GETALLEMAILS";

    private Map<String, String> actions;

    public Controller() {
        actions = new HashMap<>();
        actions.put("GET_\\/", GET_ALL_CONTACTS);
        actions.put("POST_\\/", CREATE_CONTACT);
        actions.put("GET_\\/\\d+", GET_CONTACT_BY_ID);
        actions.put("PUT_\\/\\d+\\/edit-form", UPDATE_CONTACT);               //UPDATE BAD FIX edit-form
        //actions.put("GET_\\/contacts/new-form", "NEWCONTACTFORM");
        actions.put("GET_\\/image", GET_IMAGE_FOR_CONTACT);
        actions.put("GET_\\/file", GET_FILE);
        actions.put("GET_\\/\\d+\\/edit-form", EDIT_CONTACT_FORM);
        actions.put("DELETE_", DELETE_CONTACT);
        actions.put("DELETE_\\/", DELETE_CONTACT);
        actions.put("DELETE_\\/\\d+", DELETE_CONTACT);
        actions.put("GET_\\/emails", GET_ALL_EMAILS);
        //urls with and without /?
    }

    void processString(String parsedUrl, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Process string:" + parsedUrl);
        String action = "";

        for (String key : actions.keySet()){
            if (parsedUrl != null && !parsedUrl.isEmpty() && parsedUrl.matches(key)){
                action = actions.get(key);
                break;
            }
        }
        System.out.println("ACTION: " + action);

        switch (action) {
            case "":
            case GET_ALL_CONTACTS:
                System.out.println("switch get all");
                new ShowAllContactsAction().execute(request, response);
                break;
            case CREATE_CONTACT:
                System.out.println("switch create");
                new CreateContactAction().execute(request, response);
                break;
            case GET_CONTACT_BY_ID:
            case EDIT_CONTACT_FORM:
                System.out.println("switch get by id");
                new GetContactByIdAction().execute(request, response);
                break;
            case GET_IMAGE_FOR_CONTACT:
                System.out.println("switch get image for contact");
                new GetImageForContactAction().execute(request, response);
                break;
            case GET_FILE:
                System.out.println("switch get file");
                new GetFileAction().execute(request, response);
                break;
            case UPDATE_CONTACT:
                System.out.println("switch update contact");
                new UpdateContactAction().execute(request, response);
                break;
            case DELETE_CONTACT:
                System.out.println("switch delete contact");
                new DeleteContactAction().execute(request, response);
                break;
            case GET_ALL_EMAILS:
                System.out.println("switch get all emails");
                new SendEmailAction().execute(request, response);
                break;
            default:
                new ShowAllContactsAction().execute(request, response);
                break;
        }

    }

}
