package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.entities.Address;

import static java.util.Objects.isNull;

public class AddressMapper {

    public static AddressDTO toDTO(Address entity) {
        if (isNull(entity)) {
            return null;
        }
        return AddressDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .complement(entity.getComplement())
                .neighborhood(entity.getNeighborhood())
                .zipCode(entity.getZipCode())
                .city(CityMapper.toDTO(entity.getCity()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Address toEntity(AddressDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return Address.builder()
                .id(dto.getId())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .neighborhood(dto.getNeighborhood())
                .zipCode(dto.getZipCode())
                .city(CityMapper.toEntity(dto.getCity()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
