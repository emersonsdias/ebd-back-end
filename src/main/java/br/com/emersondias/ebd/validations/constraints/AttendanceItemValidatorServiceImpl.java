package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.AttendanceItemDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.ItemRepository;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.AttendanceItemDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AttendanceItemValidatorServiceImpl implements Validator<AttendanceItemDTO>, ConstraintValidator<AttendanceItemDTOValidator, AttendanceItemDTO> {

    private final LessonRepository lessonRepository;
    private final ItemRepository itemRepository;

    @Override
    public ValidationResult<AttendanceItemDTO> validate(AttendanceItemDTO attendanceItemDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateQuantity(attendanceItemDTO, errors);
        validateAttendanceId(attendanceItemDTO, errors);
        validateItemId(attendanceItemDTO, errors);

        return new DefaultValidationResult<>(attendanceItemDTO, errors);
    }

    @Override
    public boolean isValid(AttendanceItemDTO attendanceItemDTO, ConstraintValidatorContext context) {
        var validationResult = validate(attendanceItemDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateItemId(AttendanceItemDTO attendanceItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "itemId";
        final var FIELD_VALUE = attendanceItemDTO.getItemId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O item da aula não pode ser nulo");
            return;
        }
        if (itemRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada o item com o id '" + FIELD_VALUE + "'");
        }
    }

    private void validateAttendanceId(AttendanceItemDTO attendanceItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "attendanceId";
        final var FIELD_VALUE = attendanceItemDTO.getAttendanceId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A aula associada ao item não pode ser nula");
            return;
        }

        if (lessonRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada a aula com o id '" + FIELD_VALUE + "' para associar ao item");
        }
    }

    private void validateQuantity(AttendanceItemDTO attendanceItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "quantity";
        final var FIELD_VALUE = attendanceItemDTO.getQuantity();

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
