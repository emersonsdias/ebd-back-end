package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.VisitorItemDTO;
import br.com.emersondias.ebd.entities.Item;
import br.com.emersondias.ebd.entities.Visitor;
import br.com.emersondias.ebd.entities.VisitorItem;

import java.util.Optional;

public class VisitorItemMapper {

    public static VisitorItemDTO toDTO(VisitorItem entity) {
        return VisitorItemDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .visitorId(Optional.ofNullable(entity.getVisitor()).map(Visitor::getId).orElse(null))
                .itemId(Optional.ofNullable(entity.getItem()).map(Item::getId).orElse(null))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static VisitorItem toEntity(VisitorItemDTO dto) {
        return VisitorItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .visitor(Visitor.builder().id(dto.getVisitorId()).build())
                .item(Item.builder().id(dto.getItemId()).build())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
