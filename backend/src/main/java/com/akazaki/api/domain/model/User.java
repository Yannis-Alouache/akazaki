package com.akazaki.api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String lastName;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String firstName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(min = 8)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean admin;
} 