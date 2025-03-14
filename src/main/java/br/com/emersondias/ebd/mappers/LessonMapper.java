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
                .number(entity.getNumber())
                .topic(entity.getTopic())
                .date(entity.getDate())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .visitors(entity.getVisitors().stream().map(VisitorMapper::toDTO).toList())
                .attendances(entity.getAttendances().stream().map(AttendanceMapper::toDTO).collect(Collectors.toSet()))
                .teachings(entity.getTeachings().stream().map(TeachingMapper::toDTO).collect(Collectors.toSet()))
                .items(entity.getItems().stream().map(LessonItemMapper::toDTO).collect(Collectors.toSet()))
                .offers(entity.getOffers().stream().map(OfferMapper::toDTO).toList())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SimpleLessonDTO toSimpleDTO(Lesson entity) {
        return SimpleLessonDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .topic(entity.getTopic())
                .date(entity.getDate())
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
                .number(dto.getNumber())
                .topic(dto.getTopic())
                .date(dto.getDate())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .visitors(Optional.ofNullable(dto.getVisitors()).orElse(Collections.emptyList()).stream().map(VisitorMapper::toEntity).toList())
                .attendances(Optional.ofNullable(dto.getAttendances()).orElse(Collections.emptySet()).stream().map(AttendanceMapper::toEntity).collect(Collectors.toSet()))
                .teachings(Optional.ofNullable(dto.getTeachings()).orElse(Collections.emptySet()).stream().map(TeachingMapper::toEntity).collect(Collectors.toSet()))
                .items(dto.getItems().stream().map(LessonItemMapper::toEntity).collect(Collectors.toSet()))
                .offers(dto.getOffers().stream().map(OfferMapper::toEntity).toList())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static Lesson toEntity(SimpleLessonDTO dto) {
        return Lesson.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .topic(dto.getTopic())
                .date(dto.getDate())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
