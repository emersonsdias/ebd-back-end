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

    OPEN_SAME_DAY(1, "OPEN_SAME_DAY", "Aberta para edição no dia da aula"),
    OPEN_ANY_DAY(2, "OPEN_ANY_DAY", "Aberta qualquer dia"),
    CLOSED(3, "CLOSED", "Fechada"),
    FINALIZED(4, "FINALIZED", "Finalizada");

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
