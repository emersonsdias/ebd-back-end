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
    private Integer number;
    private String topic;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date;
    private LessonStatus status;
    private String notes;
    private Long classroomId;
    private String classroomName;
    @Builder.Default
    private Set<AttendanceDTO> attendances = new HashSet<>();
    @Builder.Default
    private Set<TeachingDTO> teachings = new HashSet<>();
    @Builder.Default
    private Set<LessonItemDTO> items = new HashSet<>();
    @Builder.Default
    private List<OfferDTO> offers = new ArrayList<>();
    @Builder.Default
    private List<VisitorDTO> visitors = new ArrayList<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
