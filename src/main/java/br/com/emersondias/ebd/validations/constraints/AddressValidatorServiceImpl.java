package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.CityRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.AddressDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AddressValidatorServiceImpl implements Validator<AddressDTO>, ConstraintValidator<AddressDTOValidator, AddressDTO> {

    private final CityRepository cityRepository;

    @Override
    public ValidationResult<AddressDTO> validate(AddressDTO addressDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateStreet(addressDTO, errors);
        validateNumber(addressDTO, errors);
        validateComplement(addressDTO, errors);
        validateNeighborhood(addressDTO, errors);
        validateZipCode(addressDTO, errors);
        validateCity(addressDTO, errors);

        return new DefaultValidationResult<>(addressDTO, errors);
    }

    @Override
    public boolean isValid(AddressDTO addressDTO, ConstraintValidatorContext context) {
        var validationResult = validate(addressDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateCity(AddressDTO addressDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "city";
        final var FIELD_VALUE = addressDTO.getCity();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A cidade do endereço não pode ser nula");
            return;
        }
        if (cityRepository.findById(FIELD_VALUE.getId()).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A cidade do endereço com id informado não foi encontrada. Id: [" + FIELD_VALUE.getId() + "], nome: [" + FIELD_VALUE.getName() + "]");
        }
    }

    private void validateZipCode(AddressDTO addressDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "zipCode";
        final var FIELD_VALUE = addressDTO.getZipCode();

        final var LENGTH_MIN = 5;
        final var LENGTH_MAX = 12;
        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O CEP do endereço deve ter no mínimo '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O CEP do endereço pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateNeighborhood(AddressDTO addressDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "neighborhood";
        final var FIELD_VALUE = addressDTO.getNeighborhood();

        final var LENGTH_MAX = 100;
        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome do bairro do endereço pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateComplement(AddressDTO addressDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "complement";
        final var FIELD_VALUE = addressDTO.getComplement();

        final var LENGTH_MAX = 100;
        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O complemento do endereço pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateNumber(AddressDTO addressDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "number";
        final var FIELD_VALUE = addressDTO.getNumber();

        final var LENGTH_MAX = 100;

        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número do endereço pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateStreet(AddressDTO addressDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "street";
        final var FIELD_VALUE = addressDTO.getStreet();

        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da rua do endereço não pode ser nulo");
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da rua do endereço não pode ser vazio");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da rua do endereço pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void addFieldError(List<FieldMessageDTO> errors, String fieldName, Object fieldValue, String message) {
        errors.add(FieldMessageDTO.builder()
                .fieldName(fieldName)
                .fieldValue(fieldValue)
                .message(message)
                .build());
    }

}
