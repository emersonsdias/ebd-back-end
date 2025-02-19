package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.AttendanceOfferDTO;
import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.AttendanceRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.AttendanceOfferDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AttendanceOfferValidatorServiceImpl implements Validator<AttendanceOfferDTO>, ConstraintValidator<AttendanceOfferDTOValidator, AttendanceOfferDTO> {

    private final AttendanceRepository attendanceRepository;
    private final Validator<OfferDTO> offerValidator;

    @Override
    public ValidationResult<AttendanceOfferDTO> validate(AttendanceOfferDTO attendanceOfferDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateAttendanceId(attendanceOfferDTO, errors);
        validateOffer(attendanceOfferDTO, errors);

        return new DefaultValidationResult<>(attendanceOfferDTO, errors);
    }

    @Override
    public boolean isValid(AttendanceOfferDTO attendanceOfferDTO, ConstraintValidatorContext context) {
        var validationResult = validate(attendanceOfferDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateOffer(AttendanceOfferDTO attendanceOfferDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "offer";
        final var FIELD_VALUE = attendanceOfferDTO.getOffer();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        var offerValidation = offerValidator.validate(FIELD_VALUE);

        if (!offerValidation.isValid()) {
            offerValidation.getErrors().stream().peek(fm -> fm.setFieldName(FIELD_NAME)).forEach(errors::add);
        }
    }

    private void validateAttendanceId(AttendanceOfferDTO attendanceOfferDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "attendanceId";
        final var FIELD_VALUE = attendanceOfferDTO.getAttendanceId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A chamada associada a oferta não pode ser nula");
            return;
        }

        if (attendanceRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrada a chamada com o id '" + FIELD_VALUE + "' para associar a oferta");
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
