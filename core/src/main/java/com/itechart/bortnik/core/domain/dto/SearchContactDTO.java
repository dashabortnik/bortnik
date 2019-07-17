package com.itechart.bortnik.core.domain.dto;

import com.itechart.bortnik.core.domain.Address;
import com.itechart.bortnik.core.domain.BaseEntity;
import com.itechart.bortnik.core.domain.Gender;
import com.itechart.bortnik.core.domain.Marital;

import java.util.Date;

public class SearchContactDTO extends BaseEntity {

    private String surname;
    private String name;
    private String patronymic;
    private Date birthdayMoreThan;
    private Date birthdayLessThan;
    private Gender gender;
    private String nationality;
    private Marital maritalStatus;
    private Address address;

    public SearchContactDTO() {
    }

    public SearchContactDTO(String surname, String name, String patronymic, Date birthdayMoreThan, Date birthdayLessThan, Gender gender, String nationality, Marital maritalStatus, Address address) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthdayMoreThan = birthdayMoreThan;
        this.birthdayLessThan = birthdayLessThan;
        this.gender = gender;
        this.nationality = nationality;
        this.maritalStatus = maritalStatus;
        this.address = address;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthdayMoreThan() {
        return birthdayMoreThan;
    }

    public void setBirthdayMoreThan(Date birthdayMoreThan) {
        this.birthdayMoreThan = birthdayMoreThan;
    }

    public Date getBirthdayLessThan() {
        return birthdayLessThan;
    }

    public void setBirthdayLessThan(Date birthdayLessThan) {
        this.birthdayLessThan = birthdayLessThan;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Marital getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Marital maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SearchContactDTO{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdayMoreThan=" + birthdayMoreThan +
                ", birthdayLessThan=" + birthdayLessThan +
                ", gender=" + gender +
                ", nationality='" + nationality + '\'' +
                ", maritalStatus=" + maritalStatus +
                ", address=" + address +
                '}';
    }
}
