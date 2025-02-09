package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.dtos.SchoolProfileDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.SchoolProfileDTOValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class SchoolProfileValidatorServiceImpl implements Validator<SchoolProfileDTO>, ConstraintValidator<SchoolProfileDTOValidator, SchoolProfileDTO> {

    private final HttpServletRequest request;
    private final Validator<AddressDTO> addressValidator;

    @Override
    public ValidationResult<SchoolProfileDTO> validate(SchoolProfileDTO schoolProfileDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(schoolProfileDTO, errors);
        validateSubtitle(schoolProfileDTO, errors);
        validateAddress(schoolProfileDTO, errors);

        return new DefaultValidationResult<>(schoolProfileDTO, errors);
    }

    @Override
    public boolean isValid(SchoolProfileDTO schoolProfileDTO, ConstraintValidatorContext context) {
        var validationResult = validate(schoolProfileDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateAddress(SchoolProfileDTO schoolProfileDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "address";
        final var FIELD_VALUE = schoolProfileDTO.getAddress();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O endereço não pode ser nulo");
            return;
        }
        errors.addAll(addressValidator.validate(FIELD_VALUE).getErrors());
    }

    private void validateSubtitle(SchoolProfileDTO schoolProfileDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "subtitle";
        final var FIELD_VALUE = schoolProfileDTO.getSubtitle();

        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            return;
        }

        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O subtítulo não pode ter mais de '" + FIELD_VALUE + "' caracteres");
        }
    }

    private void validateName(SchoolProfileDTO schoolProfileDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = schoolProfileDTO.getName();

        final var LENGTH_MIN = 2;
        final var LENGTH_MAX = 100;

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

    private void validateRoute(SchoolProfileDTO schoolProfileDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = schoolProfileDTO.getId();
        var uri = request.getRequestURI();
        var schoolProfileIdPath = URIUtils.findIdAfterPath(uri, RouteConstants.SCHOOL_PROFILES_ROUTE);
        if (nonNull(schoolProfileIdPath) && !schoolProfileIdPath.equals(FIELD_VALUE)) {
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
