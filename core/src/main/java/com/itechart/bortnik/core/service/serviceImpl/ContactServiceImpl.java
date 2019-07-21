package com.itechart.bortnik.core.service.serviceImpl;

import com.itechart.bortnik.core.database.AttachmentDao;
import com.itechart.bortnik.core.database.ContactDao;
import com.itechart.bortnik.core.database.PhoneDao;
import com.itechart.bortnik.core.database.daoImpl.AttachmentDaoImpl;
import com.itechart.bortnik.core.database.daoImpl.ContactDaoImpl;
import com.itechart.bortnik.core.database.daoImpl.PhoneDaoImpl;
import com.itechart.bortnik.core.domain.Attachment;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Phone;
import com.itechart.bortnik.core.domain.dto.FullContactDTO;
import com.itechart.bortnik.core.domain.dto.SearchContactDTO;
import com.itechart.bortnik.core.service.ContactService;
import com.itechart.bortnik.core.validation.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactServiceImpl implements ContactService {

    private ContactDao contactDaoImpl;
    private PhoneDao phoneDaoImpl;
    private AttachmentDao attachmentDaoImpl;

    public ContactServiceImpl() {
        contactDaoImpl = ContactDaoImpl.getInstance();
        phoneDaoImpl = PhoneDaoImpl.getInstance();
        attachmentDaoImpl = AttachmentDaoImpl.getInstance();
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public FullContactDTO save(FullContactDTO entity) {
        FullContactDTO insertedFullContactDTO = new FullContactDTO();

        InputValidator inputValidator = new InputValidator();
        List<String> validationErrorList = inputValidator.validateObject(entity);

        if (validationErrorList.isEmpty()) {

            Contact insertedContact = contactDaoImpl.insert(entity.getContact());
            List<Phone> insertedPhones = new ArrayList<>();
            List<Attachment> insertedAttachments = new ArrayList<>();
            //if contact insert failed, we'll get an empty contact, it this case we don't insert phones and attachments
            if (insertedContact != null) {
                int contactId = insertedContact.getId();
                List<Phone> extractedPhones = entity.getPhoneList();
                for (Phone phone : extractedPhones) {
                    phone.setContactId(contactId);
                    insertedPhones.add(phoneDaoImpl.insert(phone));
                }
                List<Attachment> extractedAttachments = entity.getAttachmentList();
                for (Attachment attachment : extractedAttachments) {
                    attachment.setContactId(contactId);
                    insertedAttachments.add(attachmentDaoImpl.insert(attachment));
                }
                insertedFullContactDTO.setContact(insertedContact);
                insertedFullContactDTO.setPhoneList(insertedPhones);
                insertedFullContactDTO.setAttachmentList(insertedAttachments);
            }
        } else {
            insertedFullContactDTO.setErrorList(validationErrorList);
            //delete saved photo and attachments by links
            String photoLink = entity.getContact().getPhotoLink();
            File file = new File(photoLink);
            if (file.delete()) { //if deleted, returns true
                logger.info("Image of new contact was deleted.");
            } else {
                logger.warn("Deleting image of new contact failed.");
            }
            List<Attachment> attachments = entity.getAttachmentList();
            for (Attachment attachment : attachments) {
                File attach = new File(attachment.getLink());
                if (attach.delete()) { //if deleted, returns true
                    logger.info("Attachment of new contact was deleted.");
                } else {
                    logger.warn("Deleting attachment of new contact failed.");
                }
            }
        }
        return insertedFullContactDTO;
    }

    @Override
    public FullContactDTO update(FullContactDTO entity) {

        Contact insertedContact = contactDaoImpl.update(entity.getContact());
        //if contact insert failed, we'll get an empty contact, it this case we don't insert phones and attachments
        if (insertedContact != null) {
            int contactId = insertedContact.getId();
            List<Phone> extractedPhones = entity.getPhoneList();
            List<Phone> insertedPhones = new ArrayList<>();
            for (Phone phone : extractedPhones) {
                phone.setContactId(contactId);
                insertedPhones.add(phoneDaoImpl.insert(phone));
            }
            List<Attachment> extractedAttachments = entity.getAttachmentList();
            List<Attachment> insertedAttachments = new ArrayList<>();
            for (Attachment attachment : extractedAttachments) {
                attachment.setContactId(contactId);
                insertedAttachments.add(attachmentDaoImpl.insert(attachment));
            }
            return new FullContactDTO(insertedContact, insertedPhones, insertedAttachments, null);
        } else {
            return new FullContactDTO();
        }

    }

    @Override
    public void remove(int id) {
        contactDaoImpl.delete(id);
    }

    @Override
    public List<Contact> findAllContacts(int offset, int limit) {
        return contactDaoImpl.readAll(offset, limit);
    }

    @Override
    public List<Contact> findContactsByCriteria(SearchContactDTO search, int offset, int limit) {
        return contactDaoImpl.readByCriteria(search, offset, limit);
    }

    @Override
    public FullContactDTO findContactById(int id) {
        Contact fetchedContact = contactDaoImpl.readById(id);
        List<Phone> phoneList = phoneDaoImpl.readAllById(id);
        List<Attachment> attachmentList = attachmentDaoImpl.readAllById(id);
        FullContactDTO fullContact = new FullContactDTO(fetchedContact, phoneList, attachmentList, null);
        return fullContact;
    }

    @Override
    public List<Contact> findContactByBirthday() {
        return contactDaoImpl.readByBirthday();
    }

    @Override
    public int countAllContacts(){
        return contactDaoImpl.countContacts();
    }

    @Override
    public int countAllContactsByCriteria(SearchContactDTO search){
        return contactDaoImpl.countContactsByCriteria(search);
    }

    @Override
    public String findEmailById(int id){ return contactDaoImpl.readEmailById(id); }

}
