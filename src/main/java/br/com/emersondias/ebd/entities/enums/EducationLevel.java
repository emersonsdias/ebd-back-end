package br.com.emersondias.ebd.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum EducationLevel {

    ELEMENTARY(1, "ELEMENTARY"),
    MIDDLE_SCHOOL(2, "MIDDLE_SCHOOL"),
    HIGH_SCHOOL(3, "HIGH_SCHOOL"),
    TECHNICAL(4, "TECHNICAL"),
    INCOMPLETE_HIGHER_EDUCATION(5, "INCOMPLETE_HIGHER_EDUCATION"),
    HIGHER_EDUCATION(6, "HIGHER_EDUCATION"),
    POSTGRADUATE(7, "POSTGRADUATE");

    private final int cod;
    private final String description;

    public static EducationLevel toEnum(Integer cod) {
        if (Objects.isNull(cod)) {
            return null;
        }
        return Stream.of(EducationLevel.values())
                .filter(r -> r.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
