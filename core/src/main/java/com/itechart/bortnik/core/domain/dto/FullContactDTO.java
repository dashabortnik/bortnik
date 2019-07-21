package com.itechart.bortnik.core.domain.dto;

import com.itechart.bortnik.core.domain.Attachment;
import com.itechart.bortnik.core.domain.BaseEntity;
import com.itechart.bortnik.core.domain.Contact;
import com.itechart.bortnik.core.domain.Phone;

import java.util.List;

public class FullContactDTO extends BaseEntity {
    private Contact contact;
    private List<Phone> phoneList;
    private List<Attachment> attachmentList;
    private List<String> errorList;

    public FullContactDTO(Contact contact, List<Phone> phoneList, List<Attachment> attachmentList, List<String> errorList) {
        this.contact = contact;
        this.phoneList = phoneList;
        this.attachmentList = attachmentList;
        this.errorList = errorList;
    }

    public FullContactDTO() {
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String toString() {
        return "FullContactDTO{" +
                "contact=" + contact +
                ", phoneList=" + phoneList +
                ", attachmentList=" + attachmentList +
                ", errorList=" + errorList +
                '}';
    }
}
