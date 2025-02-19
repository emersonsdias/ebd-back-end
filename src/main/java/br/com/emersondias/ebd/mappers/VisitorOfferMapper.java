package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.VisitorOfferDTO;
import br.com.emersondias.ebd.entities.Visitor;
import br.com.emersondias.ebd.entities.VisitorOffer;

import java.util.Optional;

public class VisitorOfferMapper {

    public static VisitorOfferDTO toDTO(VisitorOffer entity) {
        return VisitorOfferDTO.builder()
                .id(entity.getId())
                .visitorId(Optional.ofNullable(entity.getVisitor()).map(Visitor::getId).orElse(null))
                .offer(OfferMapper.toDTO(entity.getOffer()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static VisitorOffer toEntity(VisitorOfferDTO dto) {
        return VisitorOffer.builder()
                .id(dto.getId())
                .visitor(Visitor.builder().id(dto.getId()).build())
                .offer(OfferMapper.toEntity(dto.getOffer()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
