package com.itechart.bortnik.core.domain.dto;

import com.itechart.bortnik.core.domain.Contact;

import java.util.List;

public class PaginationContactListDTO {

    private List<Contact> contactList;
    private int pageNumber;
    private int pageSize;
    private int totalSize;
    private int totalQuantity;
    private SearchContactDTO searchedContact;

    public PaginationContactListDTO(){
    }

    public PaginationContactListDTO(List<Contact> contactList, int pageNumber, int pageSize, int totalSize, int totalQuantity, SearchContactDTO searchedContact) {
        this.contactList = contactList;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.totalQuantity = totalQuantity;
        this.searchedContact = searchedContact;
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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public SearchContactDTO getSearchedContact() {
        return searchedContact;
    }

    public void setSearchedContact(SearchContactDTO searchedContact) {
        this.searchedContact = searchedContact;
    }

    @Override
    public String toString() {
        return "PaginationContactListDTO{" +
                "contactList=" + contactList +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalSize=" + totalSize +
                ", totalQuantity=" + totalQuantity +
                ", searchedContact=" + searchedContact +
                '}';
    }
}
