package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.VisitorDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.Visitor;

import java.util.Optional;

public class VisitorMapper {

    public static VisitorDTO toDTO(Visitor entity) {
        return VisitorDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Visitor toEntity(VisitorDTO dto) {
        return Visitor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lesson(Lesson.builder().id(dto.getLessonId()).build())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
