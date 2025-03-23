package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.UserDTO;
import br.com.emersondias.ebd.entities.User;

import static java.util.Objects.isNull;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        if (isNull(entity)) {
            return null;
        }
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(entity.getRoles())
                .person(PersonMapper.toDTO(entity.getPerson()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .roles(dto.getRoles())
                .person(PersonMapper.toEntity(dto.getPerson()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
