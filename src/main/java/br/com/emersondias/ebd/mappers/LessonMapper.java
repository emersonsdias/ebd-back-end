package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.SimpleLessonDTO;
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
                .attendances(entity.getAttendances().stream().map(AttendanceMapper::toDTO).collect(Collectors.toSet()))
                .build();
    }

    public static SimpleLessonDTO toSimpleDTO(Lesson entity) {
        return SimpleLessonDTO.builder()
                .id(entity.getId())
                .lessonNumber(entity.getLessonNumber())
                .notes(entity.getNotes())
                .classroomId(Optional.ofNullable(entity.getClassroom()).map(Classroom::getId).orElse(null))
                .build();
    }

    public static Lesson toEntity(LessonDTO dto) {
        return Lesson.builder()
                .id(dto.getId())
                .lessonNumber(dto.getLessonNumber())
                .notes(dto.getNotes())
                .classroom(Classroom.builder().id(dto.getClassroomId()).build())
                .visitors(dto.getVisitors().stream().map(VisitorMapper::toEntity).toList())
                .attendances(dto.getAttendances().stream().map(AttendanceMapper::toEntity).collect(Collectors.toSet()))
                .build();
    }

}
