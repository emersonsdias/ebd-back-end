package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.ClassroomPersonDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.ClassroomPersonDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ClassroomPersonValidatorServiceImpl implements Validator<ClassroomPersonDTO>, ConstraintValidator<ClassroomPersonDTOValidator, ClassroomPersonDTO> {

    private final PersonRepository personRepository;

    @Override
    public ValidationResult<ClassroomPersonDTO> validate(ClassroomPersonDTO classroomPersonDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validatePersonId(classroomPersonDTO, errors);

        return new DefaultValidationResult<>(classroomPersonDTO, errors);
    }

    private void validatePersonId(ClassroomPersonDTO classroomPersonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "personId";
        final var FIELD_VALUE = classroomPersonDTO.getPersonId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A pessoa não pode ser nula");
            return;
        }

        if (personRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A pessoa com o id '" + FIELD_VALUE + "' não foi encontrada");
        }
    }

    @Override
    public boolean isValid(ClassroomPersonDTO classroomPersonDTO, ConstraintValidatorContext context) {
        var validationResult = validate(classroomPersonDTO);

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
