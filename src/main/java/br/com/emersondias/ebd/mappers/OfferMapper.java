package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.Offer;

import java.util.Optional;

public class OfferMapper {

    public static OfferDTO toDTO(Offer entity) {
        return OfferDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Offer toEntity(OfferDTO dto) {
        return Offer.builder()
                .id(dto.getId())
                .amount(dto.getAmount())
                .lesson(Lesson.builder().id(dto.getId()).build())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
