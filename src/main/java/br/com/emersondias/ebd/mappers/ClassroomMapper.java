package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.entities.Classroom;

import java.util.stream.Collectors;

public class ClassroomMapper {

    public static ClassroomDTO toDTO(Classroom entity) {
        return ClassroomDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ageRange(AgeRangeMapper.toDTO(entity.getAgeRange()))
                .teachers(entity.getTeachers().stream().map(TeacherMapper::toDTO).collect(Collectors.toSet()))
                .students(entity.getStudents().stream().map(StudentMapper::toDTO).collect(Collectors.toSet()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Classroom toEntity(ClassroomDTO dto) {
        return Classroom.builder()
                .id(dto.getId())
                .name(dto.getName())
                .ageRange(AgeRangeMapper.toEntity(dto.getAgeRange()))
                .teachers(dto.getTeachers().stream().map(TeacherMapper::toEntity).collect(Collectors.toSet()))
                .students(dto.getStudents().stream().map(StudentMapper::toEntity).collect(Collectors.toSet()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
