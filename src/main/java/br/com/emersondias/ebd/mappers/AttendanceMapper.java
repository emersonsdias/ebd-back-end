package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AttendanceDTO;
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
                .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
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
                .lesson(Lesson.builder().id(dto.getLessonId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
