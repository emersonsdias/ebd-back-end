package br.com.emersondias.ebd.entities.enums.converters;

import br.com.emersondias.ebd.entities.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender attribute) {
        return isNull(attribute) ? null : attribute.getCod();
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        return Gender.toEnum(dbData);
    }
}
