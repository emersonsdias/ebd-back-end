package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.ItemDTO;
import br.com.emersondias.ebd.entities.Item;

public class ItemMapper {

    public static ItemDTO toDTO(Item entity) {
        return ItemDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Item toEntity(ItemDTO dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .icon(dto.getIcon())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
