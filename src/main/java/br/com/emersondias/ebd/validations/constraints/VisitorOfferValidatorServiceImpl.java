package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.dtos.VisitorOfferDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.VisitorOfferDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class VisitorOfferValidatorServiceImpl implements Validator<VisitorOfferDTO>, ConstraintValidator<VisitorOfferDTOValidator, VisitorOfferDTO> {

    private final Validator<OfferDTO> offerValidator;

    @Override
    public ValidationResult<VisitorOfferDTO> validate(VisitorOfferDTO visitorOfferDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateOffer(visitorOfferDTO, errors);

        return new DefaultValidationResult<>(visitorOfferDTO, errors);
    }

    @Override
    public boolean isValid(VisitorOfferDTO visitorOfferDTO, ConstraintValidatorContext context) {
        var validationResult = validate(visitorOfferDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateOffer(VisitorOfferDTO visitorOfferDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "offer";
        final var FIELD_VALUE = visitorOfferDTO.getOffer();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        var offerValidation = offerValidator.validate(FIELD_VALUE);

        if (!offerValidation.isValid()) {
            offerValidation.getErrors().stream().peek(fm -> fm.setFieldName(FIELD_NAME)).forEach(errors::add);
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
