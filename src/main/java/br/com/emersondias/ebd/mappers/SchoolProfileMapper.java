package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.SchoolProfileDTO;
import br.com.emersondias.ebd.entities.SchoolProfile;

public class SchoolProfileMapper {

    public static SchoolProfileDTO toDTO(SchoolProfile entity) {
        return SchoolProfileDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .subtitle(entity.getSubtitle())
                .address(AddressMapper.toDTO(entity.getAddress()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SchoolProfile toEntity(SchoolProfileDTO dto) {
        return SchoolProfile.builder()
                .id(dto.getId())
                .name(dto.getName())
                .subtitle(dto.getSubtitle())
                .address(AddressMapper.toEntity(dto.getAddress()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
