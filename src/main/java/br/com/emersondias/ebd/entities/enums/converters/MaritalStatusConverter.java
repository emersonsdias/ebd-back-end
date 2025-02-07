package br.com.emersondias.ebd.entities.enums.converters;

import br.com.emersondias.ebd.entities.enums.MaritalStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class MaritalStatusConverter implements AttributeConverter<MaritalStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MaritalStatus attribute) {
        return isNull(attribute) ? null : attribute.getCod();
    }

    @Override
    public MaritalStatus convertToEntityAttribute(Integer dbData) {
        return MaritalStatus.toEnum(dbData);
    }
}
