package com.itechart.bortnik.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetEmailTemplateAction implements BaseAction {

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(GetContactByIdAction.class);

    private static final String HAPPY_BIRTHDAY_EMAIL = "Happy_Birthday_Email";
    private static final String HAPPY_NEW_YEAR_EMAIL = "Happy_New_Year_Email";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String template = request.getHeader("template");
        logger.info("Email template {} was requested by user.", template);

        STGroup group = new STGroupDir("/emailTemplates");
        ST st = null;

        switch (template) {
            case HAPPY_BIRTHDAY_EMAIL:
                st = group.getInstanceOf("happyBirthdayEmail");
                break;
            case HAPPY_NEW_YEAR_EMAIL:
                st = group.getInstanceOf("happyNewYearEmail");
                break;
            default:
                logger.warn("Requested template doesn't exist.");
                break;
        }

        if (st!=null) {

            String result = st.render();
            System.out.println(result);
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();

            try (PrintWriter out = response.getWriter()) {
                mapper.writeValue(out, result);
                logger.info("Rendered email template {} was sent to browser.", template);
            } catch (IOException e) {
                logger.error("Error with email template output to browser: ", e);
            }
        }

    }


}
