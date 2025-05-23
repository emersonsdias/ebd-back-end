package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.SimpleLessonDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Lesson;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class LessonMapper {

    public static LessonDTO toDTO(Lesson entity) {
        if (isNull(entity)) {
            return null;
        }
        var classroomOpt = Optional.ofNullable(entity.getClassroom());
        return LessonDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .topic(entity.getTopic())
                .date(entity.getDate())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .classroomId(classroomOpt.map(Classroom::getId).orElse(null))
                .classroomName(classroomOpt.map(Classroom::getName).orElse(null))
                .visitors(Optional.ofNullable(entity.getVisitors()).orElse(Collections.emptyList()).stream().map(VisitorMapper::toDTO).toList())
                .attendances(Optional.ofNullable(entity.getAttendances()).orElse(Collections.emptySet()).stream().map(AttendanceMapper::toDTO).collect(Collectors.toSet()))
                .teachings(Optional.ofNullable(entity.getTeachings()).orElse(Collections.emptySet()).stream().map(TeachingMapper::toDTO).collect(Collectors.toSet()))
                .items(Optional.ofNullable(entity.getItems()).orElse(Collections.emptySet()).stream().map(LessonItemMapper::toDTO).collect(Collectors.toSet()))
                .offers(Optional.ofNullable(entity.getOffers()).orElse(Collections.emptyList()).stream().map(OfferMapper::toDTO).toList())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SimpleLessonDTO toSimpleDTO(Lesson entity) {
        if (isNull(entity)) {
            return null;
        }
        var classroomOpt = Optional.ofNullable(entity.getClassroom());
        return SimpleLessonDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .topic(entity.getTopic())
                .date(entity.getDate())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .classroomId(classroomOpt.map(Classroom::getId).orElse(null))
                .classroomName(classroomOpt.map(Classroom::getName).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Lesson toEntity(LessonDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        var lesson = Lesson.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .topic(dto.getTopic())
                .date(dto.getDate())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).name(dto.getClassroomName()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();

        lesson.setVisitors(Optional.ofNullable(dto.getVisitors()).orElse(Collections.emptyList()).stream().map(VisitorMapper::toEntity).toList());
        lesson.setAttendances(Optional.ofNullable(dto.getAttendances()).orElse(Collections.emptySet()).stream().map(AttendanceMapper::toEntity).collect(Collectors.toSet()));
        lesson.setTeachings(Optional.ofNullable(dto.getTeachings()).orElse(Collections.emptySet()).stream().map(TeachingMapper::toEntity).collect(Collectors.toSet()));
        lesson.setItems(Optional.ofNullable(dto.getItems()).orElse(Collections.emptySet()).stream().map(LessonItemMapper::toEntity).collect(Collectors.toSet()));
        lesson.setOffers(Optional.ofNullable(dto.getOffers()).orElse(Collections.emptyList()).stream().map(OfferMapper::toEntity).toList());

        return lesson;
    }

    public static Lesson toEntity(SimpleLessonDTO dto) {
        if (isNull(dto)) {
            return null;
        }
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
