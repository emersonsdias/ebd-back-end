package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SimpleClassroomDTO implements Serializable {

    private Long id;
    private String name;
    private AgeRangeDTO ageRange;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
