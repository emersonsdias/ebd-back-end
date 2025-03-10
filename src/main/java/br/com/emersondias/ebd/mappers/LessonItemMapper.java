package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonItemDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.LessonItem;

import java.util.Optional;

public class LessonItemMapper {

    public static LessonItemDTO toDTO(LessonItem entity) {
        return LessonItemDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
                .item(Optional.ofNullable(entity.getItem()).map(ItemMapper::toDTO).orElse(null))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static LessonItem toEntity(LessonItemDTO dto) {
        return LessonItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .lesson(Lesson.builder().id(dto.getLessonId()).build())
                .item(Optional.ofNullable(dto.getItem()).map(ItemMapper::toEntity).orElse(null))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
