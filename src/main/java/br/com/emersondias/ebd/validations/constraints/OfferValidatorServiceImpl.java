package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.OfferDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class OfferValidatorServiceImpl implements Validator<OfferDTO>, ConstraintValidator<OfferDTOValidator, OfferDTO> {

    private final LessonRepository lessonRepository;

    @Override
    public ValidationResult<OfferDTO> validate(OfferDTO offerDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateAmount(offerDTO, errors);
        validateLessonId(offerDTO, errors);

        return new DefaultValidationResult<>(offerDTO, errors);
    }

    @Override
    public boolean isValid(OfferDTO offerDTO, ConstraintValidatorContext context) {
        var validationResult = validate(offerDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateLessonId(OfferDTO offerDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonId";
        final var FIELD_VALUE = offerDTO.getLessonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A aula não pode ser nula");
            return;
        }

        if (lessonRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada a aula com o id '" + FIELD_VALUE + "'");
        }
    }

    private void validateAmount(OfferDTO offerDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "amount";
        final var FIELD_VALUE = offerDTO.getAmount();

        final var AMOUNT_MIN = BigDecimal.ZERO;
        final var AMOUNT_MAX = BigDecimal.valueOf(999999999);

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O valor da oferta não pode ser nulo");
            return;
        }

        if (FIELD_VALUE.compareTo(AMOUNT_MIN) < 0) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O valor da oferta não pode ser menor que '" + AMOUNT_MIN + "'");
        }
        if (FIELD_VALUE.compareTo(AMOUNT_MAX) > 0) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O valor da oferta não pode ser maior que '" + AMOUNT_MAX + "'");
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
