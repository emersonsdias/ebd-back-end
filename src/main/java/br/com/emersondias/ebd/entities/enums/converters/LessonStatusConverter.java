package br.com.emersondias.ebd.entities.enums.converters;

import br.com.emersondias.ebd.entities.enums.LessonStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class LessonStatusConverter implements AttributeConverter<LessonStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LessonStatus attribute) {
        return isNull(attribute) ? null : attribute.getCod();
    }

    @Override
    public LessonStatus convertToEntityAttribute(Integer dbData) {
        return LessonStatus.toEnum(dbData);
    }
}
