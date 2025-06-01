package com.akazaki.api.infrastructure.web.mapper.address;

import com.akazaki.api.domain.model.Address;
import com.akazaki.api.infrastructure.web.dto.response.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressResponseMapper {

    public AddressResponse toResponse(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressResponse(
            address.getId(),
            address.getLastName(),
            address.getFirstName(),
            address.getStreetNumber(),
            address.getStreet(),
            address.getAddressComplement(),
            address.getPostalCode(),
            address.getCity(),
            address.getCountry()
        );
    }
}
