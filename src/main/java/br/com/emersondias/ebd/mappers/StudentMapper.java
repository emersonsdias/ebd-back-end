package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.StudentDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Student;

import java.util.Optional;

import static java.util.Objects.isNull;

public class StudentMapper {

    public static StudentDTO toDTO(Student entity) {
        if (isNull(entity)) {
            return null;
        }
        return StudentDTO.builder()
                .id(entity.getId())
                .academicPeriodStart(entity.getAcademicPeriodStart())
                .academicPeriodEnd(entity.getAcademicPeriodEnd())
                .person(Optional.ofNullable(entity.getPerson()).map(PersonMapper::toDTO).orElse(null))
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Student toEntity(StudentDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return Student.builder()
                .id(dto.getId())
                .academicPeriodStart(dto.getAcademicPeriodStart())
                .academicPeriodEnd(dto.getAcademicPeriodEnd())
                .person(Optional.ofNullable(dto.getPerson()).map(PersonMapper::toEntity).orElse(null))
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
