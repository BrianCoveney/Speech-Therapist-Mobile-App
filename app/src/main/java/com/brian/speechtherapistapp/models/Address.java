package com.brian.speechtherapistapp.models;

/**
 * Created by brian on 27/01/18.
 */

public final class Address {

    private String street;
    private String city;
    private String county;
    private String country;


    public Address() { }

    public Address(String street, String city, String county, String country) {
        this.street = street;
        this.city = city;
        this.county = county;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
