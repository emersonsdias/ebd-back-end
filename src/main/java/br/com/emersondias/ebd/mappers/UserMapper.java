package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.UserDTO;
import br.com.emersondias.ebd.entities.User;
import br.com.emersondias.ebd.entities.enums.UserRole;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(entity.getRoles())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .roles(dto.getRoles().stream().map(UserRole::getCod).toList())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
