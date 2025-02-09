package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.AttendanceDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AttendanceDTOValidator
public class AttendanceDTO implements Serializable {

    private Long id;
    private boolean present;
    private UUID studentId;
    private Long lessonId;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
