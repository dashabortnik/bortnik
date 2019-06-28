package com.itechart.bortnik.core.domain;

import java.util.Date;
import java.util.Objects;

public class Attachment extends BaseEntity{

    private String name;
    private String link;
    private Date uploadDate;
    private int contactId;

    public Attachment(int id) {
        super(id);
    }

    public Attachment(int id, String name, String link, Date uploadDate, int contactId) {
        super(id);
        this.name = name;
        this.link = link;
        this.uploadDate = uploadDate;
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return contactId == that.contactId &&
                Objects.equals(name, that.name) &&
                Objects.equals(link, that.link) &&
                Objects.equals(uploadDate, that.uploadDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link, uploadDate, contactId);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", uploadDate=" + uploadDate +
                ", contactId=" + contactId +
                '}';
    }
}
