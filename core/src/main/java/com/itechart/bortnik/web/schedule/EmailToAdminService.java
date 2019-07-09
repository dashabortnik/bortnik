package com.itechart.bortnik.web.schedule;

import com.itechart.bortnik.core.domain.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class EmailToAdminService {

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(EmailToAdminService.class);

    public void sendEmail(List<Contact> contactList) {

        String sourceEmail = "contacts.app.2019@gmail.com";
        String password = "App.contacts.2019";
        String adminEmail = "soleildasha@gmail.com";

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
            message.setSubject("Notification about birthdays");
            message.setText(msg.toString());
            //send message
            Transport.send(message);
            logger.info("Notification about contacts who have birthday today was sent to admin email.");
        } catch (AddressException e) {
            logger.error("Error with address: ", e);
        } catch (MessagingException e) {
            logger.error("Error with email: ", e);
        }

    }

}
