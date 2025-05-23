package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.AgeRangeDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AgeRangeDTOValidator
public class AgeRangeDTO implements Serializable {

    private Long id;
    private String name;
    private Integer minAge;
    private Integer maxAge;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
