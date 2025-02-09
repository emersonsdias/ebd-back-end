package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.PhoneNumberDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.utils.ValidationUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.PhoneNumberDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class PhoneNumberValidatorServiceImpl implements Validator<PhoneNumberDTO>, ConstraintValidator<PhoneNumberDTOValidator, PhoneNumberDTO> {

    @Override
    public ValidationResult<PhoneNumberDTO> validate(PhoneNumberDTO phoneNumberDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateAreaCode(phoneNumberDTO, errors);
        validatePhoneNumber(phoneNumberDTO, errors);

        return new DefaultValidationResult<>(phoneNumberDTO, errors);
    }

    private void validatePhoneNumber(PhoneNumberDTO phoneNumberDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "phoneNumber";
        final var FIELD_VALUE = phoneNumberDTO.getPhoneNumber();

        final var LENGTH_MIN = 8;
        final var LENGTH_MAX = 20;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número de telefone não pode ser nulo");
            return;
        }

        if (!ValidationUtils.isOnlyNumbers(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número de telefone deve conter apenas números, valor informado: '" + FIELD_VALUE + "'");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número de telefone deve ter pelo menos '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número de telefone pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }


    }

    private void validateAreaCode(PhoneNumberDTO phoneNumberDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "areaCode";
        final var FIELD_VALUE = phoneNumberDTO.getAreaCode();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O código de área não pode ser nulo");
            return;
        }
        final List<String> validDDDs = Arrays.asList("11", "12", "13", "14", "15", "16", "17", "18", "19", "21", "22", "24",
                "27", "28", "31", "32", "33", "34", "35", "37", "38", "41", "42", "43", "44", "45", "46", "47", "48",
                "49", "51", "53", "54", "55", "61", "62", "63", "64", "65", "66", "67", "68", "69", "71", "73", "74",
                "75", "77", "79", "81", "82", "83", "84", "85", "86", "87", "88", "89", "91", "92", "93", "94", "95",
                "96", "97", "98", "99");
        if (!validDDDs.contains(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "o código de área '" + FIELD_VALUE + "' não é válido");
        }
    }

    @Override
    public boolean isValid(PhoneNumberDTO phoneNumberDTO, ConstraintValidatorContext context) {
        var validationResult = validate(phoneNumberDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void addFieldError(List<FieldMessageDTO> errors, String fieldName, Object fieldValue, String message) {
        errors.add(FieldMessageDTO.builder()
                .fieldName(fieldName)
                .fieldValue(fieldValue)
                .message(message)
                .build());
    }

}
