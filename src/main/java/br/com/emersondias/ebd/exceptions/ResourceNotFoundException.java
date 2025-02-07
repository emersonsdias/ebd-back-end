package br.com.emersondias.ebd.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String term;
    private final Class<?> classType;

    public ResourceNotFoundException(UUID id, Class<?> classType) {
        super("Resource not found, id: [" + id.toString() + "], class name: [" + classType.getSimpleName() + "]");
        this.term = id.toString();
        this.classType = classType;
    }

    public ResourceNotFoundException(Long id, Class<?> classType) {
        super("Resource not found, id: [" + id.toString() + "], class name: [" + classType.getSimpleName() + "]");
        this.term = id.toString();
        this.classType = classType;
    }

}
