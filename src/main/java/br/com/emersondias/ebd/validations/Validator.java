package br.com.emersondias.ebd.validations;

public interface Validator<T> {

    ValidationResult<T> validate(T t);

}
