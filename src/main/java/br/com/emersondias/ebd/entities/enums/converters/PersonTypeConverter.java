package br.com.emersondias.ebd.entities.enums.converters;

import br.com.emersondias.ebd.entities.enums.PersonType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class PersonTypeConverter implements AttributeConverter<PersonType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PersonType attribute) {
        return isNull(attribute) ? null : attribute.getCod();
    }

    @Override
    public PersonType convertToEntityAttribute(Integer dbData) {
        return PersonType.toEnum(dbData);
    }
}
