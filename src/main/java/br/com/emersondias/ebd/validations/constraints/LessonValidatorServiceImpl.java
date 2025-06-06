package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.AttendanceDTO;
import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.VisitorDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.ClassroomRepository;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.LessonDTOValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class LessonValidatorServiceImpl implements Validator<LessonDTO>, ConstraintValidator<LessonDTOValidator, LessonDTO> {

    private final HttpServletRequest request;
    private final ClassroomRepository classroomRepository;
    private final Validator<VisitorDTO> visitorValidator;
    private final Validator<AttendanceDTO> attendanceValidator;

    @Override
    public ValidationResult<LessonDTO> validate(LessonDTO lessonDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateNumber(lessonDTO, errors);
        validateDate(lessonDTO, errors);
        validateNotes(lessonDTO, errors);
        validateClassroomId(lessonDTO, errors);
        validateVisitors(lessonDTO, errors);
        validateAttendance(lessonDTO, errors);

        return new DefaultValidationResult<>(lessonDTO, errors);
    }

    private void validateAttendance(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "attendances";
        final var FIELD_VALUE = lessonDTO.getAttendances();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        FIELD_VALUE.stream()
                .map(attendanceValidator::validate)
                .filter(v -> !v.isValid())
                .map(ValidationResult::getErrors)
                .peek(e -> e.forEach(fm -> fm.setFieldName(FIELD_NAME)))
                .forEach(errors::addAll);
    }

    private void validateVisitors(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "visitors";
        final var FIELD_VALUE = lessonDTO.getVisitors();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        FIELD_VALUE.stream()
                .peek(visitorDTO -> visitorDTO.setLessonId(lessonDTO.getId()))
                .map(visitorValidator::validate)
                .filter(v -> !v.isValid())
                .map(ValidationResult::getErrors)
                .peek(e -> e.forEach(fm -> fm.setFieldName(FIELD_NAME)))
                .forEach(errors::addAll);
    }

    private void validateClassroomId(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "classroomId";
        final var FIELD_VALUE = lessonDTO.getClassroomId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A turma associada a aula não pode ser nula");
            return;
        }

        if (classroomRepository.findById(FIELD_VALUE).isEmpty()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi possível encontrar a turma com o id: '" + FIELD_VALUE + "' para a associar a aula");
        }
    }

    private void validateNotes(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "notes";
        final var FIELD_VALUE = lessonDTO.getNotes();

        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            return;
        }

        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A observação da aula não pode conter mais do que '" + LENGTH_MAX + "' caracteres");
        }

    }

    private void validateDate(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonDate";
        final var FIELD_VALUE = lessonDTO.getDate();

        final var YEARS_LIMIT = 2;
        final var DATE_MIN = LocalDate.now().minusYears(YEARS_LIMIT);
        final var DATE_MAX = LocalDate.now().plusYears(YEARS_LIMIT);

        if (isNull(FIELD_VALUE)) {
            return;
        }

        if (FIELD_VALUE.isBefore(DATE_MIN)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não é possível adicionar ou editar aulas com mais de '" + YEARS_LIMIT + "' anos passados");
        }

        if (FIELD_VALUE.isAfter(DATE_MAX)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não é possível adicionar ou editar aulas com mais de '" + YEARS_LIMIT + "' anos futuros");
        }

    }

    private void validateNumber(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "lessonNumber";
        final var FIELD_VALUE = lessonDTO.getNumber();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número da aula não pode ser nulo");
            return;
        }
        if (FIELD_VALUE < 0) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O número da aula não pode ser menor que zero");
        }
    }

    @Override
    public boolean isValid(LessonDTO lessonDTO, ConstraintValidatorContext context) {
        var validationResult = validate(lessonDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateRoute(LessonDTO lessonDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = lessonDTO.getId();
        var uri = request.getRequestURI();
        var lessonIdPath = URIUtils.findIdAfterPath(uri, RouteConstants.LESSONS_ROUTE);
        if (nonNull(lessonIdPath) && !lessonIdPath.equals(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O id informado não confere com o id da rota");
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
