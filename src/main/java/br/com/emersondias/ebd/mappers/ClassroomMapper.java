package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.entities.Classroom;

import java.util.stream.Collectors;

public class ClassroomMapper {

    public static ClassroomDTO toDTO(Classroom entity) {
        return ClassroomDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .teachers(entity.getTeachers().stream().map(TeacherMapper::toDTO).collect(Collectors.toSet()))
                .teachers(entity.getStudents().stream().map(StudentMapper::toDTO).collect(Collectors.toSet()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Classroom toEntity(ClassroomDTO entity) {
        return Classroom.builder()
                .id(entity.getId())
                .name(entity.getName())
                .teachers(entity.getTeachers().stream().map(TeacherMapper::toEntity).collect(Collectors.toSet()))
                .students(entity.getStudents().stream().map(StudentMapper::toEntity).collect(Collectors.toSet()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
