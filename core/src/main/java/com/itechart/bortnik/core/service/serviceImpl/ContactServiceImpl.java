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
import com.mchange.v1.util.ListUtils;
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
        FullContactDTO updatedFullContactDTO = new FullContactDTO();
        Contact updatedContact = contactDaoImpl.update(entity.getContact());
        //if contact insert failed, we'll get an empty contact, it this case we don't insert phones and attachments
        if (updatedContact != null) {
            int contactId = updatedContact.getId();
            //get phoneList for update and extract all non-zero ids
            List<Phone> extractedPhones = entity.getPhoneList();
            List<Integer> extractedNewPhoneId = new ArrayList<>();
            for (Phone phone : extractedPhones) {
                int id = phone.getId();
                if (id != 0) {
                    extractedNewPhoneId.add(id);
                }
            }
            //get existing phones and extract their ids
            List<Phone> currentPhones = phoneDaoImpl.readAllById(contactId);
            List<Integer> currentPhoneId = new ArrayList<>();
            for (Phone phone : currentPhones) {
                currentPhoneId.add(phone.getId());
            }
            //subtract id arrays to find out if user deleted any phones
            if (currentPhoneId != null && !currentPhoneId.isEmpty()) {

                if(extractedNewPhoneId != null && !extractedNewPhoneId.isEmpty()){
                    currentPhoneId.removeAll(extractedNewPhoneId);
                }
                for (Integer id : currentPhoneId) {
                    phoneDaoImpl.delete(id);
                }
            }
            //update existing or insert new phones
            List<Phone> updatedPhones = new ArrayList<>();
            for (Phone phone : extractedPhones) {
                int phoneId = phone.getId();
                if (phoneId == 0) {
                    updatedPhones.add(phoneDaoImpl.insert(phone));
                } else {
                    updatedPhones.add(phoneDaoImpl.update(phone));
                }

            }
            //get atachmentList for update and extract all non-zero ids
            List<Attachment> extractedAttachments = entity.getAttachmentList();
            List<Integer> extractedNewAttachmentId = new ArrayList<>();
            for (Attachment attachment : extractedAttachments) {
                int id = attachment.getId();
                if (id != 0) {
                    extractedNewAttachmentId.add(id);
                }
            }
            //get existing attachments and extract their ids
            List<Attachment> currentAttachments = attachmentDaoImpl.readAllById(contactId);
            List<Integer> currentAttachmentId = new ArrayList<>();
            for (Attachment attachment : currentAttachments) {
                currentAttachmentId.add(attachment.getId());
            }
            //subtract id arrays to find out if user deleted any attachments
            if (currentAttachmentId != null && !currentAttachmentId.isEmpty()) {
                //if user deleted all existing attachments, we need to delete every attach from currentAttachmentId
                if(extractedNewAttachmentId != null && !extractedNewAttachmentId.isEmpty()){
                    currentAttachmentId.removeAll(extractedNewAttachmentId);
                }
                for (Integer id : currentAttachmentId) {
                    attachmentDaoImpl.delete(id);
                }
            }
            //update existing or insert new phones
            List<Attachment> updatedAttachments = new ArrayList<>();
            for (Attachment attachment : extractedAttachments) {
                int attachmentId = attachment.getId();
                if (attachmentId == 0) {
                    updatedAttachments.add(attachmentDaoImpl.insert(attachment));
                } else {
                    updatedAttachments.add(attachmentDaoImpl.update(attachment));
                }
            }
            updatedFullContactDTO.setContact(updatedContact);
            updatedFullContactDTO.setPhoneList(updatedPhones);
            updatedFullContactDTO.setAttachmentList(updatedAttachments);
            //errorList stays null
        }
        return updatedFullContactDTO;
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
    public int countAllContacts() {
        return contactDaoImpl.countContacts();
    }

    @Override
    public int countAllContactsByCriteria(SearchContactDTO search) {
        return contactDaoImpl.countContactsByCriteria(search);
    }

    @Override
    public String findEmailById(int id) {
        return contactDaoImpl.readEmailById(id);
    }

}
