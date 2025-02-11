package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.TeacherDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.TeacherDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TeacherValidatorServiceImpl implements Validator<TeacherDTO>, ConstraintValidator<TeacherDTOValidator, TeacherDTO> {

    private final PersonRepository personRepository;

    @Override
    public ValidationResult<TeacherDTO> validate(TeacherDTO teacherDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validatePersonId(teacherDTO, errors);

        return new DefaultValidationResult<>(teacherDTO, errors);
    }

    private void validatePersonId(TeacherDTO teacherDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "personId";
        final var FIELD_VALUE = teacherDTO.getPersonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A pessoa não pode ser nula");
            return;
        }

        if (personRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A pessoa com o id '" + FIELD_VALUE + "' não foi encontrada");
        }
    }

    @Override
    public boolean isValid(TeacherDTO teacherDTO, ConstraintValidatorContext context) {
        var validationResult = validate(teacherDTO);

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void addFieldError(List<FieldMessageDTO> errors, String fieldName, Object fieldValue, String message) {
        errors.add(FieldMessageDTO.builder()
                .fieldName(fieldName)
                .fieldValue(fieldValue)
                .message(message)
                .build());
    }

}
