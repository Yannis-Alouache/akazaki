package com.akazaki.api.domain.model;

public class Address {
    private Long id;
    private String lastName;
    private String firstName;
    private String streetNumber;
    private String street;
    private String addressComplement;
    private String postalCode;
    private String city;
    private String country;

    private Address(
        Long id,
        String lastName,
        String firstName,
        String streetNumber,
        String street,
        String addressComplement,
        String postalCode,
        String city,
        String country
    ) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.streetNumber = streetNumber;
        this.street = street;
        this.addressComplement = addressComplement;
        this.postalCode = postalCode;
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

    // Getters
    public Long getId() { return id; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getStreetNumber() { return streetNumber; }
    public String getStreet() { return street; }
    public String getAddressComplement() { return addressComplement; }
    public String getPostalCode() { return postalCode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    

    @Override
    public String toString() {
        return "Address{" +
                    "id=" + id +
                    ", lastName='" + lastName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", streetNumber='" + streetNumber + '\'' +
                    ", street='" + street + '\'' +
                    ", addressComplement='" + addressComplement + '\'' +
                    ", postalCode='" + postalCode + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                '}';
    }

    public static class Builder {
        private Long id;
        private String lastName;
        private String firstName;
        private String streetNumber;
        private String street;
        private String addressComplement;
        private String postalCode;
        private String city;
        private String country;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder streetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }
        
        public Builder addressComplement(String addressComplement) {
            this.addressComplement = addressComplement;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(id, lastName, firstName, streetNumber, street, addressComplement, postalCode, city, country);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}