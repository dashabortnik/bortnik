package com.itechart.bortnik.core.domain.dto;

import com.itechart.bortnik.core.domain.BaseEntity;

import java.util.List;

public class EmailListDTO extends BaseEntity {

    private List<String> emailList;

    public EmailListDTO(List<String> emailList) {
        this.emailList = emailList;
    }

    public EmailListDTO() {
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    @Override
    public String toString() {
        return "EmailListDTO{" +
                "emailList=" + emailList +
                '}';
    }
}
