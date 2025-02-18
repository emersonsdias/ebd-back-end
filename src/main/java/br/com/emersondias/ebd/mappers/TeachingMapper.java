package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.TeachingDTO;
import br.com.emersondias.ebd.entities.Teacher;
import br.com.emersondias.ebd.entities.Teaching;

public class TeachingMapper {

    public static TeachingDTO toDTO(Teaching entity) {
        return TeachingDTO.builder()
                .id(entity.getId())
                .teacherId(entity.getTeacher().getId())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Teaching toEntity(TeachingDTO dto) {
        return Teaching.builder()
                .id(dto.getId())
                .teacher(Teacher.builder().id(dto.getTeacherId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
