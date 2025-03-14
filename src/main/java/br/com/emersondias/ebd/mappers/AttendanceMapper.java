package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AttendanceDTO;
import br.com.emersondias.ebd.dtos.SimpleLessonDTO;
import br.com.emersondias.ebd.entities.Attendance;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.Student;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class AttendanceMapper {

    public static AttendanceDTO toDTO(Attendance entity) {
        if (isNull(entity)) {
            return null;
        }
        var attendance = AttendanceDTO.builder()
                .id(entity.getId())
                .present(entity.isPresent())
                .lesson(LessonMapper.toSimpleDTO(entity.getLesson()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        if (nonNull(entity.getStudent())) {
            attendance.setStudentId(entity.getStudent().getId());
            if (nonNull(entity.getStudent().getPerson())) {
                attendance.setStudentName(entity.getStudent().getPerson().getName());
            }
        }
        return attendance;
    }

    public static Attendance toEntity(AttendanceDTO dto) {
        if (isNull(dto)) {
            return null;
        }
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
