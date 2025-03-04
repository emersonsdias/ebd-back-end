package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.StudentDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@StudentDTOValidator
public class StudentDTO implements Serializable {

    private UUID id;
    private UUID personId;
    private String personName;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
