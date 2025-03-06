package br.com.emersondias.ebd.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Schema(enumAsRef = true)
public enum LessonStatus {

    OPEN(1, "OPEN", "Aberta"),
    CLOSED(2, "CLOSED", "Fechada"),
    REPORTED(3, "REPORTED", "Relatório enviado");

    private final int cod;
    private final String description;
    private final String translation;

    public static LessonStatus toEnum(Integer cod) {
        if (Objects.isNull(cod)) {
            return null;
        }
        return Stream.of(LessonStatus.values())
                .filter(r -> r.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
