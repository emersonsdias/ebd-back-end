package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.entities.enums.LessonStatus;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SimpleLessonDTO implements Serializable {

    private Long id;
    private Integer number;
    private String topic;
    private LocalDate date;
    private LessonStatus status;
    private String notes;
    private Long classroomId;
    private String classroomName;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
