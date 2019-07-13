package com.itechart.bortnik.core.domain.dto;

import com.itechart.bortnik.core.domain.Contact;

import java.util.List;

public class PaginationContactList {

    private List<Contact> contactList;
    private int pageNumber;
    private int pageSize;
    private int totalSize;

    public PaginationContactList(){
    }

    public PaginationContactList(List<Contact> contactList, int pageNumber, int pageSize, int totalSize) {
        this.contactList = contactList;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "PaginationContactList{" +
                "contactList=" + contactList +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalSize=" + totalSize +
                '}';
    }
}
