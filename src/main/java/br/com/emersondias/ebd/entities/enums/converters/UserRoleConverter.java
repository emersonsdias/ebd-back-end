package br.com.emersondias.ebd.entities.enums.converters;

import br.com.emersondias.ebd.entities.enums.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole attribute) {
        return isNull(attribute) ? null : attribute.getCod();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        return UserRole.toEnum(dbData);
    }
}
