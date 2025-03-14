package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonItemDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.LessonItem;

import java.util.Optional;

import static java.util.Objects.isNull;

public class LessonItemMapper {

    public static LessonItemDTO toDTO(LessonItem entity) {
        if (isNull(entity)) {
            return null;
        }
        return LessonItemDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
                .item(Optional.ofNullable(entity.getItem()).map(ItemMapper::toDTO).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static LessonItem toEntity(LessonItemDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return LessonItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .lesson(Lesson.builder().id(dto.getLessonId()).build())
                .item(Optional.ofNullable(dto.getItem()).map(ItemMapper::toEntity).orElse(null))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
