package com.itechart.bortnik.web.action;

import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class SendEmailAction implements BaseAction {

    private ContactService contactService;

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(SendEmailAction.class);


    public SendEmailAction() {
        contactService = new ContactServiceImpl();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String emails = "";
        String topic = "";
        String body = "";

        List<FileItem> items = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            items = upload.parseRequest(request);

            cycle:
            for (FileItem item : items) {

                String fieldName = item.getFieldName();

                switch (fieldName) {
                    case "emails":
                        emails = item.getString("UTF-8");
                        break;
                    case "topic":
                        topic = item.getString("UTF-8");
                        break;
                    case "body":
                        body = item.getString("UTF-8");
                        break;
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        Properties props = new Properties();
        //compose message and fetch properties for smtp server and emails
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("emailConfig.properties")) {
            props.load(input);
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(props.getProperty("sourceEmail"), props.getProperty("password"));
                        }
                    });
            MimeMessage message = new MimeMessage(session);
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(emails));
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setSubject(topic, "UTF-8");
            message.setText(body, "UTF-8");
            //send message
            Transport.send(message);
            logger.info("Email to requested contacts was sent.");
            new ShowAllContactsAction().execute(request, response);
        } catch (AddressException e) {
            logger.error("Error with address: ", e);
        } catch (MessagingException e) {
            logger.error("Error with email: ", e);
        } catch (FileNotFoundException e) {
            logger.error("EmailConfig property file was not found: ", e);
        } catch (IOException e) {
            logger.error("Error with access to properties: ", e);
        }
    }
}