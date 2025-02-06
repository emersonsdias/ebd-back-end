package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.PhoneNumberDTO;
import br.com.emersondias.ebd.entities.PhoneNumber;

public class PhoneNumberMapper {

    public static PhoneNumberDTO toDTO(PhoneNumber entity) {
        return PhoneNumberDTO.builder()
                .id(entity.getId())
                .areaCode(entity.getAreaCode())
                .phoneNumber(entity.getPhoneNumber())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static PhoneNumber toEntity(PhoneNumberDTO dto) {
        return PhoneNumber.builder()
                .id(dto.getId())
                .areaCode(dto.getAreaCode())
                .phoneNumber(dto.getPhoneNumber())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
