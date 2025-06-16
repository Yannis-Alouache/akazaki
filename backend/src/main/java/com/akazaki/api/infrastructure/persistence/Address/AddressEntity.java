package com.akazaki.api.infrastructure.persistence.Address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String streetNumber;

    @Column(nullable = false)
    private String street;

    @Column(nullable = true)
    private String addressComplement;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;
}