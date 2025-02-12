package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AttendanceDTO;
import br.com.emersondias.ebd.dtos.SimpleLessonDTO;
import br.com.emersondias.ebd.entities.Attendance;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.Student;

import java.util.Optional;

public class AttendanceMapper {

    public static AttendanceDTO toDTO(Attendance entity) {
        return AttendanceDTO.builder()
                .id(entity.getId())
                .present(entity.isPresent())
                .studentId(Optional.ofNullable(entity.getStudent()).map(Student::getId).orElse(null))
                .lesson(Optional.ofNullable(entity.getLesson()).map(LessonMapper::toSimpleDTO).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Attendance toEntity(AttendanceDTO dto) {
        return Attendance.builder()
                .id(dto.getId())
                .present(dto.isPresent())
                .student(Student.builder().id(dto.getStudentId()).build())
                .lesson(Lesson.builder().id(Optional.ofNullable(dto.getLesson()).map(SimpleLessonDTO::getId).orElse(null)).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
