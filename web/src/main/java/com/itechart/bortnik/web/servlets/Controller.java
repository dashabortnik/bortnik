package com.itechart.bortnik.web.servlets;

import com.itechart.bortnik.core.action.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private Map<String, String> actions;

    public Controller() {
        actions = new HashMap<>();
        actions.put("GET_\\/", "GETALLCONTACTS");
        actions.put("POST_\\/", "CREATECONTACT");
        actions.put("GET_\\/\\d+", "GETCONTACTBYID");
        actions.put("PUT_\\/\\d+\\/edit-form", "UPDATECONTACT");               //UPDATE BAD FIX edit-form
        //actions.put("GET_\\/contacts/new-form", "NEWCONTACTFORM");
        actions.put("GET_\\/image", "GETIMAGEFORCONTACT");
        actions.put("GET_\\/\\d+\\/edit-form", "EDITCONTACTFORM");
        actions.put("DELETE_", "DELETECONTACT");
        actions.put("DELETE_\\/", "DELETECONTACT");
        actions.put("DELETE_\\/\\d+", "DELETECONTACT");
        actions.put("GET_\\/emails", "GETALLEMAILS");
        //urls with and without /?
        //.+\/img\/.*\.jpg
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
            case "GETALLCONTACTS":
                System.out.println("switch get all");
                new ShowAllContactsAction().execute(request, response);
                break;
            case "CREATECONTACT":
                System.out.println("switch create");
                new CreateContactAction().execute(request, response);
                break;
            case "GETCONTACTBYID":
            case "EDITCONTACTFORM":
                System.out.println("switch get by id");
                new GetContactByIdAction().execute(request, response);
                break;
            case "GETIMAGEFORCONTACT":
                System.out.println("switch get image for contact");
                new GetImageForContactAction().execute(request, response);
                break;
            case "UPDATECONTACT":
                System.out.println("switch update contact");
                new UpdateContactAction().execute(request, response);
                break;
            case "DELETECONTACT":
                System.out.println("switch delete contact");
                new DeleteContactAction().execute(request, response);
                break;
            case "GETALLEMAILS":
                System.out.println("switch get all emails");
                new SendEmailAction().execute(request, response);
                break;

            default:
                new ShowAllContactsAction().execute(request, response);
        }

    }

}
