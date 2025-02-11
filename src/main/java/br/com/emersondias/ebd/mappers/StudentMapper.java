package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.StudentDTO;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.Student;

import java.util.Optional;

public class StudentMapper {

    public static StudentDTO toDTO(Student entity) {
        var studentOpt = Optional.ofNullable(entity.getPerson());
        return StudentDTO.builder()
                .id(entity.getId())
                .personId(studentOpt.map(Person::getId).orElse(null))
                .personName(studentOpt.map(Person::getName).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Student toEntity(StudentDTO dto) {
        return Student.builder()
                .id(dto.getId())
                .person(Person.builder().id(dto.getPersonId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
