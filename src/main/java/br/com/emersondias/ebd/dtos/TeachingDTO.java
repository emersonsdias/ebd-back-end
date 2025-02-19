package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.TeachingDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@TeachingDTOValidator
public class TeachingDTO implements Serializable {

    private Long id;
    private UUID teacherId;
    private SimpleLessonDTO lesson;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
