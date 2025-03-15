package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.dtos.SimpleClassroomDTO;
import br.com.emersondias.ebd.entities.Classroom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class ClassroomMapper {

    public static ClassroomDTO toDTO(Classroom entity) {
        if (isNull(entity)) {
            return null;
        }
        return ClassroomDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ageRange(AgeRangeMapper.toDTO(entity.getAgeRange()))
                .teachers(Optional.ofNullable(entity.getTeachers()).orElse(new HashSet<>()).stream().map(TeacherMapper::toDTO).collect(Collectors.toSet()))
                .students(Optional.ofNullable(entity.getStudents()).orElse(new HashSet<>()).stream().map(StudentMapper::toDTO).collect(Collectors.toSet()))
                .lessons(Optional.ofNullable(entity.getLessons()).orElse(new ArrayList<>()).stream().map(LessonMapper::toSimpleDTO).toList())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Classroom toEntity(ClassroomDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        var classroom = Classroom.builder()
                .id(dto.getId())
                .name(dto.getName())
                .ageRange(AgeRangeMapper.toEntity(dto.getAgeRange()))
                .teachers(Optional.ofNullable(dto.getTeachers()).orElse(new HashSet<>()).stream().map(TeacherMapper::toEntity).collect(Collectors.toSet()))
                .students(Optional.ofNullable(dto.getStudents()).orElse(new HashSet<>()).stream().map(StudentMapper::toEntity).collect(Collectors.toSet()))
                .lessons(Optional.ofNullable(dto.getLessons()).orElse(new ArrayList<>()).stream().map(LessonMapper::toEntity).toList())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
        classroom.getTeachers().forEach(t -> t.setClassroom(classroom));
        classroom.getStudents().forEach(s -> s.setClassroom(classroom));
        classroom.getLessons().forEach(l -> l.setClassroom(classroom));
        return classroom;
    }

    public static SimpleClassroomDTO toSimpleDTO(Classroom entity) {
        if (isNull(entity)) {
            return null;
        }
        return SimpleClassroomDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ageRange(AgeRangeMapper.toDTO(entity.getAgeRange()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
