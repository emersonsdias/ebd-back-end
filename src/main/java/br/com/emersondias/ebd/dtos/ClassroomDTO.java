package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.ClassroomDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ClassroomDTOValidator
public class ClassroomDTO implements Serializable {

    private Long id;
    private String name;
    private AgeRangeDTO ageRange;
    @Builder.Default
    private Set<TeacherDTO> teachers = new HashSet<>();
    @Builder.Default
    private Set<StudentDTO> students = new HashSet<>();
    @Builder.Default
    private List<SimpleLessonDTO> lessons = new ArrayList<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
