package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.TeachingDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.Teacher;
import br.com.emersondias.ebd.entities.Teaching;

import static java.util.Objects.isNull;

public class TeachingMapper {

    public static TeachingDTO toDTO(Teaching entity) {
        if (isNull(entity)) {
            return null;
        }
        return TeachingDTO.builder()
                .id(entity.getId())
                .teacherId(entity.getTeacher().getId())
                .lesson(LessonMapper.toSimpleDTO(entity.getLesson()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Teaching toEntity(TeachingDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return Teaching.builder()
                .id(dto.getId())
                .teacher(Teacher.builder().id(dto.getTeacherId()).build())
                .lesson(LessonMapper.toEntity(dto.getLesson()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
