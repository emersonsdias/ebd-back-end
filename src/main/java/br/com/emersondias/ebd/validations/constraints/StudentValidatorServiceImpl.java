package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.StudentDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.StudentDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class StudentValidatorServiceImpl implements Validator<StudentDTO>, ConstraintValidator<StudentDTOValidator, StudentDTO> {

    private final PersonRepository personRepository;

    @Override
    public ValidationResult<StudentDTO> validate(StudentDTO studentDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validatePerson(studentDTO, errors);

        return new DefaultValidationResult<>(studentDTO, errors);
    }

    private void validatePerson(StudentDTO studentDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "person";
        final var FIELD_VALUE = studentDTO.getPerson();

        if (isNull(FIELD_VALUE) || isNull(FIELD_VALUE.getId())) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A pessoa não pode ser nula");
            return;
        }

        if (personRepository.findById(FIELD_VALUE.getId()).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A pessoa com o id '" + FIELD_VALUE + "' não foi encontrada");
        }
    }

    @Override
    public boolean isValid(StudentDTO studentDTO, ConstraintValidatorContext context) {
        var validationResult = validate(studentDTO);

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
