package br.com.emersondias.ebd.validations.constraints;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.ItemDTO;
import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import br.com.emersondias.ebd.repositories.ItemRepository;
import br.com.emersondias.ebd.utils.URIUtils;
import br.com.emersondias.ebd.validations.DefaultValidationResult;
import br.com.emersondias.ebd.validations.ValidationResult;
import br.com.emersondias.ebd.validations.Validator;
import br.com.emersondias.ebd.validations.annotations.ItemDTOValidator;
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
public class ItemValidatorServiceImpl implements Validator<ItemDTO>, ConstraintValidator<ItemDTOValidator, ItemDTO> {

    private final HttpServletRequest request;
    private final ItemRepository repository;

    @Override
    public ValidationResult<ItemDTO> validate(ItemDTO itemDTO) {
        final List<FieldMessageDTO> errors = new ArrayList<>();

        validateName(itemDTO, errors);
        validateIcon(itemDTO, errors);

        return new DefaultValidationResult<>(itemDTO, errors);
    }

    @Override
    public boolean isValid(ItemDTO itemDTO, ConstraintValidatorContext context) {
        var validationResult = validate(itemDTO);
        validateRoute(validationResult.getObject(), validationResult.getErrors());

        validationResult.getErrors().forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }

    private void validateIcon(ItemDTO itemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "icon";
        final var FIELD_VALUE = itemDTO.getIcon();

        final var LENGTH_MIN = 2;
        final var LENGTH_MAX = 100;

        if (isNull(FIELD_VALUE)) {
            return;
        }
        if (FIELD_VALUE.isBlank()) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O ícone não pode ser vazio");
        }
        if (FIELD_VALUE.length() < LENGTH_MIN) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O ícone deve ter pelo menos '" + LENGTH_MIN + "' caracteres");
        }
        if (FIELD_VALUE.length() > LENGTH_MAX) {
            addFieldError(errors, FIELD_NAME, FIELD_VALUE, "O ícone pode ter no máximo '" + LENGTH_MAX + "' caracteres");
        }
    }

    private void validateName(ItemDTO itemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "name";
        final var FIELD_VALUE = itemDTO.getName();

        final var LENGTH_MIN = 2;
        final var LENGTH_MAX = 100;

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

        repository.findByName(FIELD_VALUE).ifPresent(item -> {
            if (!item.getId().equals(itemDTO.getId())) {
                addFieldError(errors, FIELD_NAME, FIELD_VALUE, "Já existe um item com o nome '" + FIELD_VALUE + "'");
            }
        });
    }

    private void validateRoute(ItemDTO itemDTO, List<FieldMessageDTO> errors) {
        final var FIELD_NAME = "id";
        final var FIELD_VALUE = itemDTO.getId();
        var uri = request.getRequestURI();
        var itemIdPath = URIUtils.findIdAfterPath(uri, RouteConstants.ITEMS_ROUTE);
        if (nonNull(itemIdPath) && !itemIdPath.equals(FIELD_VALUE)) {
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
