package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.dtos.ClassroomPersonDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.AgeRangeRepository;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.ClassroomDTOValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ClassroomValidatorServiceImpl implements Validator<ClassroomDTO>, ConstraintValidator<ClassroomDTOValidator, ClassroomDTO> {

    private final HttpServletRequest request;
    private final AgeRangeRepository ageRangeRepository;
    private final Validator<ClassroomPersonDTO> classroomPersonValidator;

    @Override
    public ValidationResult<ClassroomDTO> validate(ClassroomDTO classroomDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(classroomDTO, errors);
        validateAgeRange(classroomDTO, errors);
        validateTeachers(classroomDTO, errors);
        validateStudents(classroomDTO, errors);

        return new DefaultValidationResult<>(classroomDTO, errors);
    }

    private void validateStudents(ClassroomDTO classroomDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "students";
        final var FIELD_VALUE = classroomDTO.getTeachers();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        FIELD_VALUE.stream()
                .map(classroomPersonValidator::validate)
                .filter(v -> !v.isValid())
                .map(ValidationResult::getErrors)
                .peek(e -> e.forEach(fm -> fm.setFieldName(FIELD_NAME)))
                .forEach(errors::addAll);
    }

    private void validateTeachers(ClassroomDTO classroomDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "teachers";
        final var FIELD_VALUE = classroomDTO.getTeachers();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        FIELD_VALUE.stream()
                .map(classroomPersonValidator::validate)
                .filter(v -> !v.isValid())
                .map(ValidationResult::getErrors)
                .peek(e -> e.forEach(fm -> fm.setFieldName(FIELD_NAME)))
                .forEach(errors::addAll);
    }

    private void validateAgeRange(ClassroomDTO classroomDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "ageRange";
        final var FIELD_VALUE = classroomDTO.getAgeRange();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        if (ageRangeRepository.findById(FIELD_VALUE.getId()).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE,
                    "Não foi encontrado a faixa etária com id: '" + FIELD_VALUE.getId() + "', nome: '" + FIELD_VALUE.getName() + "'");
        }
    }

    private void validateName(ClassroomDTO classroomDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = classroomDTO.getName();

        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome da classe não pode ser nulo");
            return;
        }

        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }

    }

    private void validateRoute(ClassroomDTO classroomDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = classroomDTO.getId();
        var uri = request.getRequestURI();
        var classroomIdPath = URIUtils.findIdAfterPath(uri, RouteConstants.CLASSROOMS_ROUTE);
        if (nonNull(classroomIdPath) && !classroomIdPath.equals(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O id informado não confere com o id da rota");
        }
    }

    @Override
    public boolean isValid(ClassroomDTO classroomDTO, ConstraintValidatorContext context) {
        var validationResult = validate(classroomDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

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
