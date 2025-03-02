package br.com.emersondias.ebd.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Schema(enumAsRef = true)
public enum EducationLevel {

    ELEMENTARY(1, "ELEMENTARY", "Ensino fundamento incompleto"),
    MIDDLE_SCHOOL(2, "MIDDLE_SCHOOL", "Ensino fundamental"),
    HIGH_SCHOOL(3, "HIGH_SCHOOL", "Ensino médio"),
    TECHNICAL(4, "TECHNICAL", "Ensino técnico"),
    INCOMPLETE_HIGHER_EDUCATION(5, "INCOMPLETE_HIGHER_EDUCATION", "Superior incompleto"),
    HIGHER_EDUCATION(6, "HIGHER_EDUCATION", "Superior completo"),
    POSTGRADUATE(7, "POSTGRADUATE", "Pós graduação");

    private final int cod;
    private final String description;
    private final String translation;

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
