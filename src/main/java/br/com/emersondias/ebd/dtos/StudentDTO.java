package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.StudentDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@StudentDTOValidator
public class StudentDTO implements Serializable {

    private UUID id;
    private LocalDate academicPeriodStart;
    private LocalDate academicPeriodEnd;
    private PersonDTO person;
    private Long classroomId;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
