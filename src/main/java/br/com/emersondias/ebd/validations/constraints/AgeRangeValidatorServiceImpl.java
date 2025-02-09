package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.AgeRangeDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.AgeRangeRepository;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.AgeRangeDTOValidator;
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
public class AgeRangeValidatorServiceImpl implements Validator<AgeRangeDTO>, ConstraintValidator<AgeRangeDTOValidator, AgeRangeDTO> {

    private final HttpServletRequest request;
    private final AgeRangeRepository ageRangeRepository;

    @Override
    public ValidationResult<AgeRangeDTO> validate(AgeRangeDTO ageRangeDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(ageRangeDTO, errors);
        validateMinAge(ageRangeDTO, errors);
        validateMaxAge(ageRangeDTO, errors);

        return new DefaultValidationResult<>(ageRangeDTO, errors);
    }

    @Override
    public boolean isValid(AgeRangeDTO ageRangeDTO, ConstraintValidatorContext context) {
        var validationResult = validate(ageRangeDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateMaxAge(AgeRangeDTO ageRangeDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "maxAge";
        final var FIELD_VALUE = ageRangeDTO.getMaxAge();

        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE < 0) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A idade máxima não pode ser menor que zero");
        }
        if (nonNull(ageRangeDTO.getMinAge()) && FIELD_VALUE < ageRangeDTO.getMinAge()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A idade máxima não pode ser menor que a idade mínima");
        }
    }

    private void validateMinAge(AgeRangeDTO ageRangeDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "minAge";
        final var FIELD_VALUE = ageRangeDTO.getMinAge();

        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE < 0) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A idade mínima não pode ser menor que zero");
        }
    }

    private void validateName(AgeRangeDTO ageRangeDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = ageRangeDTO.getName();

        final var LENGTH_MIN = 3;
        final var LENGTH_MAX = 100;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da faixa etária não pode ser nulo");
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da faixa etária não pode ser vazio");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da faixa etária deve ter pelo menos '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da faixa etária pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }

        ageRangeRepository.findByName(FIELD_VALUE).ifPresent(ageRange -> {
            if (!ageRange.getId().equals(ageRangeDTO.getId())) {
                addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Já existe uma faixa etária cadastrada com esse mesmo nome");
            }
        });
    }

    private void validateRoute(AgeRangeDTO ageRangeDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = ageRangeDTO.getId();
        var uri = request.getRequestURI();
        var ageRangeIdPath = URIUtils.findIdAfterPath(uri, RouteConstants.AGE_RANGES_ROUTE);
        if (nonNull(ageRangeIdPath) && !ageRangeIdPath.equals(FIELD_VALUE)) {
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
