package br.com.emersondias.ebd.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum UserRole {

    ADMIN(1, "ADMIN"),
    BASIC(2, "BASIC");

    private final int cod;
    private final String description;

    public static UserRole toEnum(Integer cod) {
        if (Objects.isNull(cod)) {
            return null;
        }
        return Stream.of(UserRole.values())
                .filter(r -> r.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
