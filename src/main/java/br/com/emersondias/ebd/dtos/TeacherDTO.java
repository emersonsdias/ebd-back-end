package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.TeacherDTOValidator;
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
@TeacherDTOValidator
public class TeacherDTO implements Serializable {

    private UUID id;
    private LocalDate teachingPeriodStart;
    private LocalDate teachingPeriodEnd;
    private PersonDTO person;
    private Long classroomId;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
