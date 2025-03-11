package br.com.emersondias.ebd.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Schema(enumAsRef = true)
public enum PersonType {

    STUDENT(1, "STUDENT"),
    TEACHER(2, "TEACHER");

    private final int cod;
    private final String description;

    public static PersonType toEnum(Integer cod) {
        if (Objects.isNull(cod)) {
            return null;
        }
        return Stream.of(PersonType.values())
                .filter(r -> r.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
