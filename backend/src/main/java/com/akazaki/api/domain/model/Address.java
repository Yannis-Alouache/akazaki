package com.akazaki.api.domain.model;

public class Address {
    private Long id;
    private String lastName;
    private String firstName;
    private String streetNumber;
    private String street;
    private String addressComplement;
    private String postCode;
    private String city;
    private String country;

    private Address(
        Long id,
        String lastName,
        String firstName,
        String streetNumber,
        String street,
        String addressComplement,
        String postCode,
        String city,
        String country
    ) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.streetNumber = streetNumber;
        this.street = street;
        this.addressComplement = addressComplement;
        this.postCode = postCode;
        this.city = city;
        this.country = country;
    }

    public static Address create(
        String lastName,
        String firstName,
        String streetNumber,
        String street,
        String addressComplement,
        String postCode,
        String city,
        String country
    ) {
        return new Address(null, lastName, firstName, streetNumber, street, addressComplement, postCode, city, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                    "id=" + id +
                    ", lastName='" + lastName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", streetNumber='" + streetNumber + '\'' +
                    ", street='" + street + '\'' +
                    ", addressComplement='" + addressComplement + '\'' +
                    ", postCode='" + postCode + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                '}';
    }
}