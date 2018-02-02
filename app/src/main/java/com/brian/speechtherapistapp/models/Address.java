package com.brian.speechtherapistapp.models;

/**
 * Created by brian on 27/01/18.
 */

public final class Address {

    private final String street;
    private final String city;
    private final String county;
    private final String country;

    public Address(String street, String city, String county, String country) {
        this.street = street;
        this.city = city;
        this.county = county;
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
