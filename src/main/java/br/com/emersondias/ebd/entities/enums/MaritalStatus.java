package br.com.emersondias.ebd.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum MaritalStatus {

    SINGLE(1, "SINGLE", "Solteiro(a)"),
    MARRIED(2, "MARRIED", "Casado(a)"),
    DIVORCED(3, "DIVORCED", "Divorciado(a)"),
    WIDOWED(4, "WIDOWED", "Viúvo(a)");

    private final int cod;
    private final String description;
    private final String translate;

    public static MaritalStatus toEnum(Integer cod) {
        if (Objects.isNull(cod)) {
            return null;
        }
        return Stream.of(MaritalStatus.values())
                .filter(r -> r.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
