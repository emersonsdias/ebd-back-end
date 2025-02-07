package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.entities.Address;

public class AddressMapper {

    public static AddressDTO toDTO(Address entity) {
        return AddressDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .complement(entity.getComplement())
                .neighborhood(entity.getNeighborhood())
                .zipCode(entity.getZipCode())
                .city(CityMapper.toDTO(entity.getCity()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Address toEntity(AddressDTO dto) {
        return Address.builder()
                .id(dto.getId())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .neighborhood(dto.getNeighborhood())
                .zipCode(dto.getZipCode())
                .city(CityMapper.toEntity(dto.getCity()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
