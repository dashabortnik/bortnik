package com.itechart.bortnik.core.domain;

import java.util.Objects;

public class Address extends BaseEntity{

    private String country;
    private String city;
    private String street;
    private String postcode;

    public Address(int id) {
        super(id);
    }

    public Address(int id, String country, String city, String street, String postcode) {
        super(id);
        this.country = country;
        this.city = city;
        this.street = street;
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return country.equals(address.country) &&
                city.equals(address.city) &&
                street.equals(address.street) &&
                postcode.equals(address.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, postcode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + this.getId() + '\''+
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
