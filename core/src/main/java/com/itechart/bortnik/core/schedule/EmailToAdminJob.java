package com.itechart.bortnik.core.schedule;

import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.service.serviceImpl.ContactServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EmailToAdminJob implements Job {

    private ContactService contactService;

    public EmailToAdminJob() {
        contactService = new ContactServiceImpl();
    }

    private EmailToAdminService ETAService = new EmailToAdminService();

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(EmailToAdminJob.class);

    public void execute(JobExecutionContext context) {
        List<Contact> contactList = contactService.findContactByBirthday();
        if (!contactList.isEmpty()) {
            ETAService.sendEmail(contactList);
        } else {
            logger.info("No contacts have birthdays today.");
        }

    }

}
