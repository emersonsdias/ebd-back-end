package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.dtos.PhoneNumberDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.utils.ValidationUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.PersonDTOValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PersonValidatorServiceImpl implements Validator<PersonDTO>, ConstraintValidator<PersonDTOValidator, PersonDTO> {

    private final HttpServletRequest request;
    private final Validator<AddressDTO> addressValidator;
    private final Validator<PhoneNumberDTO> phoneNumberValidator;

    @Override
    public ValidationResult<PersonDTO> validate(PersonDTO personDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(personDTO, errors);
        validateBirthdate(personDTO, errors);
        validateEmail(personDTO, errors);
        validateAddress(personDTO, errors);
        validatePhoneNumbers(personDTO, errors);

        return new DefaultValidationResult<>(personDTO, errors);
    }

    @Override
    public boolean isValid(PersonDTO personDTO, ConstraintValidatorContext context) {
        var validationResult = validate(personDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validatePhoneNumbers(PersonDTO personDTO, List<FieldMessageDTO> errors) {
        final var FIELD_VALUE = personDTO.getPhoneNumbers();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        FIELD_VALUE.stream()
                .map(phoneNumberValidator::validate)
                .filter(v -> !v.isValid())
                .forEach(v -> errors.addAll(v.getErrors()));
    }

    private void validateAddress(PersonDTO personDTO, List<FieldMessageDTO> errors) {
        final var FIELD_VALUE = personDTO.getAddress();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        var addressValidationResult = addressValidator.validate(FIELD_VALUE);
        errors.addAll(addressValidationResult.getErrors());
    }

    private void validateEmail(PersonDTO personDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "email";
        final var FIELD_VALUE = personDTO.getEmail();

        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (!ValidationUtils.isValidEmail(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O endereço de email '" + FIELD_VALUE + "' é inválido");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O email pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }

    }

    private void validateBirthdate(PersonDTO personDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "birthdate";
        final var FIELD_VALUE = personDTO.getBirthdate();

        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE.isAfter(LocalDate.now())) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A data de nascimento não pode ser uma data futura");
        }
    }

    private void validateName(PersonDTO personDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = personDTO.getName();

        final var LENGTH_MIN = 2;
        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome não pode ser nulo");
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome não pode ser vazio");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome deve ter pelo menos '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateRoute(PersonDTO personDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = personDTO.getId();
        var uri = request.getRequestURI();
        var personIdPath = URIUtils.findUuidAfterPath(uri, RouteConstants.PEOPLE_ROUTE);
        if (nonNull(personIdPath) && !personIdPath.equals(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O id informado não confere com o id da rota");
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
