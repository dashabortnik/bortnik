package com.itechart.bortnik.web.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
public class ContactServlet extends HttpServlet {

    private Controller controller;

    public ContactServlet() {
        controller = new Controller();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(ContactServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        parseUrl(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
//        if (request.getParameterMap().containsKey("_method") && request.getParameter("_method") == "PUT") {
//            doPut(request, response);
//        } else {
            parseUrl(request, response);
//        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        parseUrl(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        parseUrl(request, response);
    }

    private void parseUrl(HttpServletRequest request, HttpServletResponse response) {
        //URI starts with /contacts, which gives us an empty String in the beginning if regex = "/contacts"
        String[] urlArray = request.getRequestURI().split("contacts", 2);
        String urlPart = request.getMethod() + "_";
/*
        if (request.getParameterMap().containsKey("_method")) {
            urlPart = request.getParameter("_method") + "_";
        } else {
            urlPart = request.getMethod() + "_";
        }
*/
        if (urlArray.length > 1) {
            urlPart += urlArray[1];
            logger.info("Incoming request: {}", urlPart);
        }
        try {
            controller.processString(urlPart, request, response);
        } catch (ServletException e) {
            e.printStackTrace();
            logger.error("Exception in parseUrl method: ", e);
        } catch (IOException e) {
            logger.error("Exception in parseUrl method: ", e);
        }
    }

}
