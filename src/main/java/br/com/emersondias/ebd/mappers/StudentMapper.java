package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.StudentDTO;
import br.com.emersondias.ebd.entities.Student;

import java.util.Optional;

public class StudentMapper {

    public static StudentDTO toDTO(Student entity) {
        return StudentDTO.builder()
                .id(entity.getId())
                .person(Optional.ofNullable(entity.getPerson()).map(PersonMapper::toDTO).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Student toEntity(StudentDTO dto) {
        return Student.builder()
                .id(dto.getId())
                .person(Optional.ofNullable(dto.getPerson()).map(PersonMapper::toEntity).orElse(null))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
