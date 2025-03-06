package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.deserializer.CustomLocalDateDeserializer;
import br.com.emersondias.ebd.entities.enums.LessonStatus;
import br.com.emersondias.ebd.validations.annotations.LessonDTOValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@LessonDTOValidator
public class LessonDTO implements Serializable {

    private Long id;
    private Integer lessonNumber;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate lessonDate;
    private LessonStatus status;
    private String notes;
    private Long classroomId;
    @Builder.Default
    private List<VisitorDTO> visitors = new ArrayList<>();
    @Builder.Default
    private Set<AttendanceDTO> attendances = new HashSet<>();
    @Builder.Default
    private Set<TeachingDTO> teachings = new HashSet<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
