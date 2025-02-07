package br.com.emersondias.ebd.entities.enums.converters;

import br.com.emersondias.ebd.entities.enums.EducationLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class EducationLevelConverter implements AttributeConverter<EducationLevel, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EducationLevel attribute) {
        return isNull(attribute) ? null : attribute.getCod();
    }

    @Override
    public EducationLevel convertToEntityAttribute(Integer dbData) {
        return EducationLevel.toEnum(dbData);
    }
}
