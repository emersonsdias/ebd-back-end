package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.location.CityDTO;
import br.com.emersondias.ebd.dtos.location.CitySimpleDTO;
import br.com.emersondias.ebd.entities.location.City;

import static java.util.Objects.isNull;

public class CityMapper {

    public static CityDTO toDTO(City entity) {
        if (isNull(entity)) {
            return null;
        }
        return CityDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .state(StateMapper.toDTO(entity.getState()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static CitySimpleDTO toSimpleDTO(City entity) {
        if (isNull(entity)) {
            return null;
        }
        return CitySimpleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static City toEntity(CityDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return City.builder()
                .id(dto.getId())
                .name(dto.getName())
                .state(StateMapper.toEntity(dto.getState()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
