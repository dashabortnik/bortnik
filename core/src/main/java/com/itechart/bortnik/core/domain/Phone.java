package com.itechart.bortnik.core.domain;

import java.util.Objects;

public class Phone extends BaseEntity{

    private String countryCode;
    private String operatorCode;
    private String phoneNumber;
    private PhoneType phoneType;
    private String comment;
    private int contactId;

    public Phone(int id) {
        super(id);
    }

    public Phone(int id, String countryCode, String operatorCode, String phoneNumber, PhoneType phoneType, String comment,
                 int contactId) {
        super(id);
        this.countryCode = countryCode;
        this.operatorCode = operatorCode;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.comment = comment;
        this.contactId = contactId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return contactId == phone.contactId &&
                Objects.equals(countryCode, phone.countryCode) &&
                Objects.equals(operatorCode, phone.operatorCode) &&
                Objects.equals(phoneNumber, phone.phoneNumber) &&
                phoneType == phone.phoneType &&
                Objects.equals(comment, phone.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, operatorCode, phoneNumber, phoneType, comment, contactId);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id='" + this.getId() + '\''+
                ", countryCode='" + countryCode + '\'' +
                ", operatorCode='" + operatorCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneType=" + phoneType +
                ", comment='" + comment + '\'' +
                ", contactId=" + contactId +
                '}';
    }
}
