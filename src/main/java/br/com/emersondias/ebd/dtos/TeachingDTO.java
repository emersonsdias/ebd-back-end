package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeachingDTO implements Serializable {

    private Long id;
    private UUID teacherId;
    private SimpleLessonDTO lesson;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
