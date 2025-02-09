package br.com.emersondias.ebd.validations;

import br.com.emersondias.ebd.dtos.errors.FieldMessageDTO;

import java.util.List;
import java.util.function.Function;

public interface ValidationResult<T> {

    T getObject();

    List<FieldMessageDTO> getErrors();

    default boolean isValid() {
        return getErrors().isEmpty();
    }

    default <X extends Throwable> void isValidOrThrow(Function<ValidationResult<T>, ? extends X> exceptionSupplier) throws X {
        if (!isValid()) {
            throw exceptionSupplier.apply(this);
        }
    }

}
