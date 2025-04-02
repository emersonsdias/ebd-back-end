package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.dtos.OfferDTOWithLesson;
import br.com.emersondias.ebd.entities.Offer;

import static java.util.Objects.isNull;

public class OfferMapper {

    public static OfferDTO toDTO(Offer entity) {
        if (isNull(entity)) {
            return null;
        }
        return OfferDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static OfferDTOWithLesson toDTOWithLesson(Offer entity) {
        if (isNull(entity)) {
            return null;
        }
        return OfferDTOWithLesson.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .lesson(LessonMapper.toDTO(entity.getLesson()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Offer toEntity(OfferDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return Offer.builder()
                .id(dto.getId())
                .amount(dto.getAmount())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
