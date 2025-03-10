package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.LessonOfferDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.LessonOffer;

import java.util.Optional;

public class LessonOfferMapper {

    public static LessonOfferDTO toDTO(LessonOffer entity) {
        return LessonOfferDTO.builder()
                .id(entity.getId())
                .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
                .offer(OfferMapper.toDTO(entity.getOffer()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static LessonOffer toEntity(LessonOfferDTO dto) {
        return LessonOffer.builder()
                .id(dto.getId())
                .lesson(Lesson.builder().id(dto.getId()).build())
                .offer(OfferMapper.toEntity(dto.getOffer()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
