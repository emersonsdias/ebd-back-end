package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.PhoneNumberDTO;
import br.com.emersondias.ebd.entities.PhoneNumber;

import static java.util.Objects.isNull;

public class PhoneNumberMapper {

    public static PhoneNumberDTO toDTO(PhoneNumber entity) {
        if (isNull(entity)) {
            return null;
        }
        return PhoneNumberDTO.builder()
                .id(entity.getId())
                .areaCode(entity.getAreaCode())
                .phoneNumber(entity.getPhoneNumber())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static PhoneNumber toEntity(PhoneNumberDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return PhoneNumber.builder()
                .id(dto.getId())
                .areaCode(dto.getAreaCode())
                .phoneNumber(dto.getPhoneNumber())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
