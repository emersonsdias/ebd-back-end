package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.UserDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.UserRepository;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.utils.ValidationUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.UserDTOValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.emersondias.ebd.utils.ValidationUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserValidatorServiceImpl implements Validator<UserDTO>, ConstraintValidator<UserDTOValidator, UserDTO> {

    private final HttpServletRequest request;
    private final UserRepository repository;

    @Override
    public ValidationResult<UserDTO> validate(UserDTO userDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(userDTO, errors);
        validateEmail(userDTO, errors);
        validatePassword(userDTO, errors);

        return new DefaultValidationResult<>(userDTO, errors);
    }

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
        var validationResult = validate(userDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validatePassword(UserDTO userDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "password";
        final var FIELD_VALUE = userDTO.getPassword();

        final var LENGTH_MIN = 8;

        if (isNull(FIELD_VALUE)) {
            if (isNull(userDTO.getId())) {
                addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A senha não pode ser vazia");
            }
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A senha não pode ser vazia");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A senha não pode ser menor que '" + LENGTH_MIN + "' caracteres");
        }
        if (!containsLetters(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A senha deve conter pelo menos uma letra");
        }
        if (!containsNumbers(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A senha deve conter pelo menos um número");
        }
        if (!containsSpecialCharacters(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "A senha deve conter pelo menos um caractere especial");
        }
    }

    private void validateEmail(UserDTO userDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "email";
        final var FIELD_VALUE = userDTO.getEmail();

        final var LENGTH_MIN = 3;
        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O email não pode ser nulo");
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O email não pode ser vazio");
        }
        if (!ValidationUtils.isValidEmail(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O endereço de email '" + FIELD_VALUE + "' não é válido");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O email deve ter pelo menos '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O email pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
        repository.findByEmailIgnoreCase(FIELD_VALUE).ifPresent(userEntity -> {
            if (!userEntity.getId().equals(userDTO.getId())) {
                addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O e-mail '" + FIELD_VALUE + "' já está em uso por outro usuário");
            }
        });
    }

    private void validateName(UserDTO userDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = userDTO.getName();

        final var LENGTH_MIN = 2;
        final var LENGTH_MAX = 255;

        if (isNull(FIELD_VALUE)) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome não pode ser nulo");
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome não pode ser vazio");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome deve ter pelo menos '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O nome pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateRoute(UserDTO userDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = userDTO.getId();
        var uri = request.getRequestURI();
        var userIdPath = URIUtils.findUuidAfterPath(uri, RouteConstants.USERS_ROUTE);
        if (nonNull(userIdPath) && !userIdPath.equals(FIELD_VALUE)) {
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
