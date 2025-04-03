package com.akazaki.api.domain.model;

public class User  {
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean admin;

    private User(
            Long id,
            String lastName,
            String firstName,
            String email,
            String password,
            String phoneNumber,
            boolean admin
    ) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
    }

    public static User create(
            Long id,
            String lastName,
            String firstName,
            String email,
            String password,
            String phoneNumber,
            boolean admin
    ) {
        return new User(
                id,
                lastName,
                firstName,
                email,
                password,
                phoneNumber,
                admin
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", admin=" + admin +
                '}';
    }
}