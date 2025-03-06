package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.SimpleLessonDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Lesson;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class LessonMapper {

    public static LessonDTO toDTO(Lesson entity) {
        return LessonDTO.builder()
                .id(entity.getId())
                .lessonNumber(entity.getLessonNumber())
                .lessonDate(entity.getLessonDate())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .visitors(entity.getVisitors().stream().map(VisitorMapper::toDTO).toList())
                .attendances(entity.getAttendances().stream().map(AttendanceMapper::toDTO).collect(Collectors.toSet()))
                .teachings(entity.getTeachings().stream().map(TeachingMapper::toDTO).collect(Collectors.toSet()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SimpleLessonDTO toSimpleDTO(Lesson entity) {
        return SimpleLessonDTO.builder()
                .id(entity.getId())
                .lessonNumber(entity.getLessonNumber())
                .lessonDate(entity.getLessonDate())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Lesson toEntity(LessonDTO dto) {
        return Lesson.builder()
                .id(dto.getId())
                .lessonNumber(dto.getLessonNumber())
                .lessonDate(dto.getLessonDate())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .visitors(Optional.ofNullable(dto.getVisitors()).orElse(Collections.emptyList()).stream().map(VisitorMapper::toEntity).toList())
                .attendances(Optional.ofNullable(dto.getAttendances()).orElse(Collections.emptySet()).stream().map(AttendanceMapper::toEntity).collect(Collectors.toSet()))
                .teachings(Optional.ofNullable(dto.getTeachings()).orElse(Collections.emptySet()).stream().map(TeachingMapper::toEntity).collect(Collectors.toSet()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static Lesson toEntity(SimpleLessonDTO dto) {
        return Lesson.builder()
                .id(dto.getId())
                .lessonNumber(dto.getLessonNumber())
                .lessonDate(dto.getLessonDate())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
