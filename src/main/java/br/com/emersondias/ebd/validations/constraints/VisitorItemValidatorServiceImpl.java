package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.VisitorItemDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.ItemRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.VisitorItemDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class VisitorItemValidatorServiceImpl implements Validator<VisitorItemDTO>, ConstraintValidator<VisitorItemDTOValidator, VisitorItemDTO> {

    private final ItemRepository itemRepository;

    @Override
    public ValidationResult<VisitorItemDTO> validate(VisitorItemDTO visitorItemDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateQuantity(visitorItemDTO, errors);
        validateItem(visitorItemDTO, errors);

        return new DefaultValidationResult<>(visitorItemDTO, errors);
    }

    @Override
    public boolean isValid(VisitorItemDTO visitorItemDTO, ConstraintValidatorContext context) {
        var validationResult = validate(visitorItemDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateItem(VisitorItemDTO visitorItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "item";
        final var FIELD_VALUE = visitorItemDTO.getItem();

        if (isNull(FIELD_VALUE) || isNull(FIELD_VALUE.getId())) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O item do visitante não pode ser nulo");
            return;
        }
        if (itemRepository.findById(FIELD_VALUE.getId()).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada o item com o id '" + FIELD_VALUE.getId() + "'");
        }
    }

    private void validateQuantity(VisitorItemDTO visitorItemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "quantity";
        final var FIELD_VALUE = visitorItemDTO.getQuantity();

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
