package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.LessonItemDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.ItemRepository;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.LessonItemDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class LessonItemValidatorServiceImpl implements Validator<LessonItemDTO>, ConstraintValidator<LessonItemDTOValidator, LessonItemDTO> {

    private final LessonRepository lessonRepository;
    private final ItemRepository itemRepository;

    @Override
    public ValidationResult<LessonItemDTO> validate(LessonItemDTO lessonItemDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateQuantity(lessonItemDTO, errors);
        validateLessonId(lessonItemDTO, errors);
        validateItemId(lessonItemDTO, errors);

        return new DefaultValidationResult<>(lessonItemDTO, errors);
    }

    @Override
    public boolean isValid(LessonItemDTO lessonItemDTO, ConstraintValidatorContext context) {
        var validationResult = validate(lessonItemDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateItemId(LessonItemDTO lessonItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonId";
        final var FIELD_VALUE = lessonItemDTO.getLessonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O item da aula não pode ser nulo");
            return;
        }
        if (itemRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada o item com o id '" + FIELD_VALUE + "'");
        }
    }

    private void validateLessonId(LessonItemDTO lessonItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonId";
        final var FIELD_VALUE = lessonItemDTO.getLessonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A aula associada ao item não pode ser nula");
            return;
        }

        if (lessonRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada a aula com o id '" + FIELD_VALUE + "' para associar ao item");
        }
    }

    private void validateQuantity(LessonItemDTO lessonItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "quantity";
        final var FIELD_VALUE = lessonItemDTO.getQuantity();

        final var QUANTITY_MIN = 0;
        final var QUANTITY_MAX = 1000;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A quantidade de items não pode ser nula");
            return;
        }
        if (FIELD_VALUE < QUANTITY_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A quantidade de items não pode ser menor que '" + QUANTITY_MIN + "'");
        }

        if (FIELD_VALUE > QUANTITY_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A quantidade de items não pode ser maior que '" + QUANTITY_MAX + "'");
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
