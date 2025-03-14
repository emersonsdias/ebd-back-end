package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.location.StateDTO;
import br.com.emersondias.ebd.entities.location.State;

import static java.util.Objects.isNull;

public class StateMapper {

    public static StateDTO toDTO(State entity) {
        if (isNull(entity)) {
            return null;
        }
        return StateDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .abbreviation(entity.getAbbreviation())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static State toEntity(StateDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return State.builder()
                .id(dto.getId())
                .name(dto.getName())
                .abbreviation(dto.getAbbreviation())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
