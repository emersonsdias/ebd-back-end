package br.com.emersondias.ebd.validations;

import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DefaultValidationResult<T> implements ValidationResult<T> {

    private final T object;
    private final List<FieldMessageDTO> errors;

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public List<FieldMessageDTO> getErrors() {
        return errors;
    }
}
