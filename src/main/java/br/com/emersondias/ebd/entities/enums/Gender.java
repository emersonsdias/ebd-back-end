package br.com.emersondias.ebd.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Gender {

    MALE(1, "MALE", "Masculino"),
    FEMALE(2, "FEMALE", "Feminino");

    private final int cod;
    private final String description;
    private final String translate;

    public static Gender toEnum(Integer cod) {
        if (Objects.isNull(cod)) {
            return null;
        }
        return Stream.of(Gender.values())
                .filter(r -> r.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
