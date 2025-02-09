package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.ClassroomDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
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
    private Set<ClassroomPersonDTO> teachers = new HashSet<>();
    @Builder.Default
    private Set<ClassroomPersonDTO> students = new HashSet<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
