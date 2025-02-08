package br.com.emersondias.ebd.dtos;

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
public class LessonDTO implements Serializable {

    private Long id;
    private Integer lessonNumber;
    private LocalDate lessonDate;
    private String notes;
    private Long classroomId;
    @Builder.Default
    private List<VisitorDTO> visitors = new ArrayList<>();
    @Builder.Default
    private List<OfferDTO> offers = new ArrayList<>();
    @Builder.Default
    private Set<LessonItemDTO> items = new HashSet<>();
    @Builder.Default
    private Set<AttendanceDTO> attendances = new HashSet<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
