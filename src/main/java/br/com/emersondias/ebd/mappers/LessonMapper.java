package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Lesson;

import java.util.Optional;
import java.util.stream.Collectors;

public class LessonMapper {

    public static LessonDTO toDTO(Lesson entity) {
        return LessonDTO.builder()
                .id(entity.getId())
                .lessonNumber(entity.getLessonNumber())
                .notes(entity.getNotes())
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .visitors(entity.getVisitors().stream().map(VisitorMapper::toDTO).toList())
                .offers(entity.getOffers().stream().map(OfferMapper::toDTO).toList())
                .items(entity.getItems().stream().map(LessonItemMapper::toDTO).collect(Collectors.toSet()))
                .attendances(entity.getAttendances().stream().map(AttendanceMapper::toDTO).collect(Collectors.toSet()))
                .build();
    }

    public static Lesson toEntity(LessonDTO dto) {
        return Lesson.builder()
                .id(dto.getId())
                .lessonNumber(dto.getLessonNumber())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .visitors(dto.getVisitors().stream().map(VisitorMapper::toEntity).toList())
                .offers(dto.getOffers().stream().map(OfferMapper::toEntity).toList())
                .items(dto.getItems().stream().map(LessonItemMapper::toEntity).collect(Collectors.toSet()))
                .attendances(dto.getAttendances().stream().map(AttendanceMapper::toEntity).collect(Collectors.toSet()))
                .build();
    }

}
