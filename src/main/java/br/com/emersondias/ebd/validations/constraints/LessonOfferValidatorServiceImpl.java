package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.LessonOfferDTO;
import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.LessonOfferDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class LessonOfferValidatorServiceImpl implements Validator<LessonOfferDTO>, ConstraintValidator<LessonOfferDTOValidator, LessonOfferDTO> {

    private final LessonRepository lessonRepository;
    private final Validator<OfferDTO> offerValidator;

    @Override
    public ValidationResult<LessonOfferDTO> validate(LessonOfferDTO lessonOfferDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateLessonId(lessonOfferDTO, errors);
        validateOffer(lessonOfferDTO, errors);

        return new DefaultValidationResult<>(lessonOfferDTO, errors);
    }

    @Override
    public boolean isValid(LessonOfferDTO lessonOfferDTO, ConstraintValidatorContext context) {
        var validationResult = validate(lessonOfferDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateOffer(LessonOfferDTO lessonOfferDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "offer";
        final var FIELD_VALUE = lessonOfferDTO.getOffer();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        var offerValidation = offerValidator.validate(FIELD_VALUE);

        if (!offerValidation.isValid()) {
            offerValidation.getErrors().stream().peek(fm -> fm.setFieldName(FIELD_NAME)).forEach(errors::add);
        }
    }

    private void validateLessonId(LessonOfferDTO lessonOfferDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonId";
        final var FIELD_VALUE = lessonOfferDTO.getLessonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A lição associada a oferta não pode ser nula");
            return;
        }

        if (lessonRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada a lição com o id '" + FIELD_VALUE + "' para associar a oferta");
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
