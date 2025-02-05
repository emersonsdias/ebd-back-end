package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.location.CityDTO;
import br.com.emersondias.ebd.dtos.location.CitySimpleDTO;
import br.com.emersondias.ebd.entities.location.City;

import static java.util.Objects.nonNull;

public class CityMapper {

    public static CityDTO toDTO(City entity) {
        var cityDTO = CityDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        if (nonNull(entity.getState())) {
            cityDTO.setState(StateMapper.toDTO(entity.getState()));
        }
        return cityDTO;
    }

    public static CitySimpleDTO toSimpleDTO(City entity) {
        return CitySimpleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static City toEntity(CityDTO dto) {
        return City.builder()
                .id(dto.getId())
                .name(dto.getName())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
