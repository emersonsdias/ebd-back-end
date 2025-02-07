package br.com.emersondias.ebd.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum MaritalStatus {

    SINGLE(1, "SINGLE"),
    MARRIED(2, "MARRIED"),
    DIVORCED(3, "DIVORCED"),
    WIDOWED(4, "WIDOWED");

    private final int cod;
    private final String description;

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
