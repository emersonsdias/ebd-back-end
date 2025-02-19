package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.TeachingDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.TeacherRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.TeachingDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TeachingValidatorServiceImpl implements Validator<TeachingDTO>, ConstraintValidator<TeachingDTOValidator, TeachingDTO> {

    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    @Override
    public ValidationResult<TeachingDTO> validate(TeachingDTO teachingDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateTeacherId(teachingDTO, errors);

        return new DefaultValidationResult<>(teachingDTO, errors);
    }

    @Override
    public boolean isValid(TeachingDTO teachingDTO, ConstraintValidatorContext context) {
        var validationResult = validate(teachingDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }


    private void validateTeacherId(TeachingDTO teachingDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "teacherId";
        final var FIELD_VALUE = teachingDTO.getTeacherId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O professor não pode ser nulo");
            return;
        }

        if (teacherRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O professor com id '" + FIELD_VALUE + "' não foi encontrado");
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
