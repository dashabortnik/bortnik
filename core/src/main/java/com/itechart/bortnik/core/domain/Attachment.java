package com.itechart.bortnik.core.domain;

import java.util.Date;
import java.util.Objects;

public class Attachment extends BaseEntity{

    private String name;
    private String link;
    private Date uploadDate;
    private String commentary;
    private int contactId;

    public Attachment(int id) {
        super(id);
    }

    public Attachment(int id, String name, String link, Date uploadDate, String commentary, int contactId) {
        super(id);
        this.name = name;
        this.link = link;
        this.uploadDate = uploadDate;
        this.commentary = commentary;
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

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id='" + this.getId() + '\''+
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", uploadDate=" + uploadDate +
                ", commentary='" + commentary + '\'' +
                ", contactId=" + contactId +
                '}';
    }
}
