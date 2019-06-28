package com.itechart.bortnik.core.action;

import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SendEmailAction implements BaseAction {

    private ContactService contactService;
    private String sourceEmail = "dasha_bortnik@mail.ru";
    private String adminEmail = "soleildasha@gmail.com";
    private String password = "xyzxyz";


    public SendEmailAction() {
        contactService = new ContactServiceImpl();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Date date = new Date();
        System.out.println("TODAY IS " + date);
        List<Contact> contactList = contactService.findContactByBirthday(date);
        System.out.println("FROM ACTION: " + contactList.toString());
        StringBuilder msg = new StringBuilder("Good morning! We have some birthdays to celebrate today: \n");
        for (Contact contact : contactList) {
            msg.append(contact.getName());
            msg.append(" ");
            msg.append(contact.getSurname());
            msg.append(", email: ");
            msg.append(contact.getEmail());
            msg.append("\n");
        }
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sourceEmail, password);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(adminEmail));
            message.setSubject("Happy Birthday!");
            message.setText(msg.toString());
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
}

//create personal message for contact
//    StringBuilder msg = new StringBuilder("Dear ");
//            msg.append(contact.getName());
//                    msg.append(" ");
//                    msg.append(contact.getSurname());
//                    msg.append("! We hope you have an amazing day today. Happy Birthday!");