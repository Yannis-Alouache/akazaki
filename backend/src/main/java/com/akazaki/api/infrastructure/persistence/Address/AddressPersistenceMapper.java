package com.akazaki.api.infrastructure.persistence.Address;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Address;

@Component
public class AddressPersistenceMapper {
    public AddressEntity toEntity(Address address) {
        if (address == null) {
            return null;
        }
        
        return AddressEntity.builder()
            .id(address.getId())
            .lastName(address.getLastName())
            .firstName(address.getFirstName())
            .streetNumber(address.getStreetNumber())
            .street(address.getStreet())
            .addressComplement(address.getAddressComplement())
            .postalCode(address.getPostalCode())
            .city(address.getCity())
            .country(address.getCountry())
            .build();
    }

    public Address toDomain(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }

        return Address.builder()
            .id(addressEntity.getId())
            .lastName(addressEntity.getLastName())
            .firstName(addressEntity.getFirstName())
            .streetNumber(addressEntity.getStreetNumber())
            .street(addressEntity.getStreet())
            .addressComplement(addressEntity.getAddressComplement())
            .postalCode(addressEntity.getPostalCode())
            .city(addressEntity.getCity())
            .country(addressEntity.getCountry())
            .build();
    }
}
