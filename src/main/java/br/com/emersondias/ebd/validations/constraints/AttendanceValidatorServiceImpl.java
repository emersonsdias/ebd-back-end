package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.dtos.AttendanceDTO;
import br.com.emersondias.ebd.dtos.AttendanceItemDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.Student;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.repositories.StudentRepository;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.AttendanceDTOValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AttendanceValidatorServiceImpl implements Validator<AttendanceDTO>, ConstraintValidator<AttendanceDTOValidator, AttendanceDTO> {

    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final Validator<AttendanceItemDTO> attendanceItemValidator;

    @Transactional(readOnly = true)
    @Override
    public ValidationResult<AttendanceDTO> validate(AttendanceDTO attendanceDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        var studentOpt = studentRepository.findById(attendanceDTO.getStudentId());
        var lessonOpt = lessonRepository.findById(attendanceDTO.getLesson().getId());

        validateStudentId(attendanceDTO, errors, studentOpt.orElse(null), lessonOpt.orElse(null));
        validateLessonId(attendanceDTO, errors, lessonOpt.orElse(null));
        validateItems(attendanceDTO, errors);

        return new DefaultValidationResult<>(attendanceDTO, errors);
    }

    private void validateItems(AttendanceDTO attendanceDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "items";
        final var FIELD_VALUE = attendanceDTO.getItems();

        if (isNull(FIELD_VALUE)) {
            return;
        }

        FIELD_VALUE.stream()
                .map(attendanceItemValidator::validate)
                .filter(v -> !v.isValid())
                .map(ValidationResult::getErrors)
                .peek(e -> e.forEach(fm -> fm.setFieldName(FIELD_NAME)))
                .forEach(errors::addAll);
    }

    private void validateLessonId(AttendanceDTO attendanceDTO, List<FieldMessageDTO> errors, Lesson lessonEntity) {
        final var FIELD_NAME = "lesson";
        final var FIELD_VALUE = attendanceDTO.getLesson();

        if (isNull(FIELD_VALUE) || isNull(FIELD_VALUE.getId())) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A aula não pode ser nula");
            return;
        }

        if (isNull(lessonEntity)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrado a aula com o id '" + FIELD_VALUE + "'");
        } else {
            if (lessonEntity.getLessonDate().isAfter(LocalDate.now())) {
                addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não é possível realizar a chamada de aulas futuras");
            }
        }
    }

    private void validateStudentId(AttendanceDTO attendanceDTO, List<FieldMessageDTO> errors, Student studentEntity, Lesson lessonEntity) {
        final var FIELD_NAME = "studentId";
        final var FIELD_VALUE = attendanceDTO.getStudentId();

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O aluno da chamada não pode ser nulo");
            return;
        }
        if (isNull(studentEntity)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não foi encontrado o estudante matriculado com o id: '" + FIELD_VALUE + "'");
        } else {
            if (nonNull(lessonEntity)) {
                if (!studentEntity.getClassroom().getId().equals(lessonEntity.getClassroom().getId())) {
                    addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Não é possível cadastrar chamada pois o aluno não está matriculado nessa turma");
                }
            }
        }
    }

    @Override
    public boolean isValid(AttendanceDTO attendanceDTO, ConstraintValidatorContext context) {
        var validationResult = validate(attendanceDTO);

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
