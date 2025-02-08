package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AgeRangeDTO;
import br.com.emersondias.ebd.entities.AgeRange;

public class AgeRangeMapper {

    public static AgeRangeDTO toDTO(AgeRange entity) {
        return AgeRangeDTO.builder()
                .id(entity.getId())
                .minAge(entity.getMinAge())
                .maxAge(entity.getMaxAge())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static AgeRange toEntity(AgeRangeDTO dto) {
        return AgeRange.builder()
                .id(dto.getId())
                .minAge(dto.getMinAge())
                .maxAge(dto.getMaxAge())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
