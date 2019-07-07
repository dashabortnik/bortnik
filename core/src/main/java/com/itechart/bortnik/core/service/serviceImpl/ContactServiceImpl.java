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
import com.itechart.bortnik.core.service.ContactService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactServiceImpl implements ContactService {

    private ContactDao contactDaoImpl;
    private PhoneDao phoneDaoImpl;
    private AttachmentDao attachmentDaoImpl;

    public ContactServiceImpl() {

        contactDaoImpl = new ContactDaoImpl();
        phoneDaoImpl = new PhoneDaoImpl();
        attachmentDaoImpl = new AttachmentDaoImpl();
    }

    @Override
    public FullContactDTO save(FullContactDTO entity) {
        Contact insertedContact = contactDaoImpl.insert(entity.getContact());
        //int contactId = insertedContact.getId();
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
            return new FullContactDTO(insertedContact, insertedPhones, insertedAttachments);
        } else {
            return new FullContactDTO();
        }
    }

    @Override
    public Contact update(Contact entity) {
        return contactDaoImpl.update(entity);
    }

    @Override
    public void remove(int id) {
        contactDaoImpl.delete(id);
    }

    @Override
    public List<Contact> findAllContacts() {
        return contactDaoImpl.readAll();
    }

    @Override
    public List<Contact> findContactsByCriteria(Contact search) {

        return contactDaoImpl.readByCriteria(search);
    }

    @Override
    public FullContactDTO findContactById(int id) {
        Contact fetchedContact = contactDaoImpl.readById(id);
        List<Phone> phoneList = phoneDaoImpl.readAllById(id);
        List<Attachment> attachmentList = attachmentDaoImpl.readAllById(id);
        FullContactDTO fullContact = new FullContactDTO(fetchedContact, phoneList, attachmentList);
        return fullContact;
    }

    @Override
    public List<Contact> findContactByBirthday(Date birthday) {

        return contactDaoImpl.readByBirthday(birthday);
    }

}
