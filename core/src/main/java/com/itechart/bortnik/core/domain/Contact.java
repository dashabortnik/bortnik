package com.itechart.bortnik.core.domain;

import java.util.Date;
import java.util.Objects;

public class Contact extends BaseEntity{

    private String surname;
    private String name;
    private String patronymic;
    private Date birthday;
    private Gender gender;
    private String nationality;
    private Marital maritalStatus;
    private String website;
    private String email;
    private String workplace;
    private String photoLink;
    private Address address;

    public Contact(int id) {
        super(id);
    }

    public Contact(int id, String surname, String name, String patronymic, Date birthday, Gender gender,
                   String nationality, Marital maritalStatus, String website, String email, String workplace, String photoLink,
                   Address address) {
        super(id);
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.gender = gender;
        this.nationality = nationality;
        this.maritalStatus = maritalStatus;
        this.website = website;
        this.email = email;
        this.workplace = workplace;
        this.photoLink = photoLink;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(surname, contact.surname) &&
                Objects.equals(name, contact.name) &&
                Objects.equals(patronymic, contact.patronymic) &&
                Objects.equals(birthday, contact.birthday) &&
                gender == contact.gender &&
                Objects.equals(nationality, contact.nationality) &&
                maritalStatus == contact.maritalStatus &&
                Objects.equals(website, contact.website) &&
                Objects.equals(email, contact.email) &&
                Objects.equals(workplace, contact.workplace) &&
                Objects.equals(photoLink, contact.photoLink) &&
                Objects.equals(address, contact.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, patronymic, birthday, gender, nationality, maritalStatus, website, email,
                workplace, photoLink, address);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + this.getId() + '\''+
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", nationality='" + nationality + '\'' +
                ", maritalStatus=" + maritalStatus +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", workplace='" + workplace + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", address=" + address +
                '}';
    }
}
