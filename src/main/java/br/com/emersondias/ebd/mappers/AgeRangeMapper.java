package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AgeRangeDTO;
import br.com.emersondias.ebd.entities.AgeRange;

import static java.util.Objects.isNull;

public class AgeRangeMapper {

    public static AgeRangeDTO toDTO(AgeRange entity) {
        if (isNull(entity)) {
            return null;
        }
        return AgeRangeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .minAge(entity.getMinAge())
                .maxAge(entity.getMaxAge())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static AgeRange toEntity(AgeRangeDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return AgeRange.builder()
                .id(dto.getId())
                .name(dto.getName())
                .minAge(dto.getMinAge())
                .maxAge(dto.getMaxAge())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
