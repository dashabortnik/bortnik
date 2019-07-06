package com.itechart.bortnik.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@MultipartConfig
public class ContactServlet extends HttpServlet {

    private Controller controller;

    public ContactServlet() {
        controller = new Controller();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Get request");
        parseUrl(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        System.out.println("Post request");
//        if (request.getParameterMap().containsKey("_method") && request.getParameter("_method") == "PUT") {
//            doPut(request, response);
//        } else {
            parseUrl(request, response);
//        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Put request");
        parseUrl(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Delete request");
        parseUrl(request, response);
    }

    private void parseUrl(HttpServletRequest request, HttpServletResponse response) {
        //System.out.println(request.getMethod());
        //System.out.println(request.getRequestURI());
        //URI starts with /contacts, which gives us an empty String in the beginning if regex = "/contacts"
        String[] urlArray = request.getRequestURI().split("contacts", 2);
        System.out.println(urlArray[0]);
        System.out.println(urlArray[1]);
        String urlPart;
//        if (request.getParameterMap().containsKey("_method")) {
//            urlPart = request.getParameter("_method") + "_";
//        } else {
            urlPart = request.getMethod() + "_";
//        }

        if (urlArray.length > 1) {
            urlPart += urlArray[1];
            System.out.println("Extracted url part---" + urlPart);
        }
        try {
            controller.processString(urlPart, request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
