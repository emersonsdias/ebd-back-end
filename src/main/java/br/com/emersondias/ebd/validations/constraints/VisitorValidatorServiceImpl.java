package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.VisitorDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.VisitorDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class VisitorValidatorServiceImpl implements Validator<VisitorDTO>, ConstraintValidator<VisitorDTOValidator, VisitorDTO> {

    private final LessonRepository lessonRepository;

    @Override
    public ValidationResult<VisitorDTO> validate(VisitorDTO visitorDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(visitorDTO, errors);
        validateLesson(visitorDTO, errors);

        return new DefaultValidationResult<>(visitorDTO, errors);
    }

    @Override
    public boolean isValid(VisitorDTO visitorDTO, ConstraintValidatorContext context) {
        var validationResult = validate(visitorDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateLesson(VisitorDTO visitorDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonId";
        final var FIELD_VALUE = visitorDTO.getLessonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A aula não pode ser nula");
            return;
        }

        if (lessonRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada a aula com o id '" + FIELD_VALUE + "'");
        }
    }

    private void validateName(VisitorDTO visitorDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = visitorDTO.getName();

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

    private void addFieldError(List<FieldMessageDTO> errors, String fieldName, Object fieldValue, String message) {
        errors.add(FieldMessageDTO.builder()
                .fieldName(fieldName)
                .fieldValue(fieldValue)
                .message(message)
                .build());
    }

}
