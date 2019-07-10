package com.itechart.bortnik.core.schedule;

import com.itechart.bortnik.core.domain.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class EmailToAdminService {

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(EmailToAdminService.class);

    public void sendEmail(List<Contact> contactList) {

        StringBuilder msg = new StringBuilder("Good morning! We have some birthdays to celebrate today: \n");
        for (Contact contact : contactList) {
            msg.append(contact.getName());
            msg.append(" ");
            msg.append(contact.getSurname());
            msg.append(", email: ");
            msg.append(contact.getEmail());
            msg.append("\n");
        }
        msg.append("Let's wish them happy birthday!");

        Properties props = new Properties();
        //compose message and fetch properties for smtp server and emails
        try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("emailConfig.properties")){
            props.load(input);
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(props.getProperty("sourceEmail"), props.getProperty("password"));
                        }
                    });
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(props.getProperty("adminEmail")));
            message.setSubject("Notification about birthdays");
            message.setText(msg.toString());
            //send message
            Transport.send(message);
            logger.info("Notification about contacts who have birthday today was sent to admin email.");
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
