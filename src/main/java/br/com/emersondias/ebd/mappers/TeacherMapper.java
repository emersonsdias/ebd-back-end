package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.ClassroomPersonDTO;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.Teacher;

import java.util.Optional;

public class TeacherMapper {

    public static ClassroomPersonDTO toDTO(Teacher entity) {
        var personOpt = Optional.ofNullable(entity.getPerson());
        return ClassroomPersonDTO.builder()
                .id(entity.getId())
                .personId(personOpt.map(Person::getId).orElse(null))
                .personName(personOpt.map(Person::getName).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Teacher toEntity(ClassroomPersonDTO dto) {
        return Teacher.builder()
                .id(dto.getId())
                .person(Person.builder().id(dto.getPersonId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
