package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LessonDTO implements Serializable {

    private Long id;
    private Integer lessonNumber;
    private LocalDate lessonDate;
    private String notes;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
