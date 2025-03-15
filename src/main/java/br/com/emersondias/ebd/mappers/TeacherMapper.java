package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.TeacherDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Teacher;

import java.util.Optional;

import static java.util.Objects.isNull;

public class TeacherMapper {

    public static TeacherDTO toDTO(Teacher entity) {
        if (isNull(entity)) {
            return null;
        }
        return TeacherDTO.builder()
                .id(entity.getId())
                .teachingPeriodStart(entity.getTeachingPeriodStart())
                .teachingPeriodEnd(entity.getTeachingPeriodEnd())
                .person(Optional.ofNullable(entity.getPerson()).map(PersonMapper::toDTO).orElse(null))
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Teacher toEntity(TeacherDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return Teacher.builder()
                .id(dto.getId())
                .teachingPeriodStart(dto.getTeachingPeriodStart())
                .teachingPeriodEnd(dto.getTeachingPeriodEnd())
                .person(Optional.ofNullable(dto.getPerson()).map(PersonMapper::toEntity).orElse(null))
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
