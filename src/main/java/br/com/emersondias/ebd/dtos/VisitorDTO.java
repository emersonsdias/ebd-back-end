package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.VisitorDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@VisitorDTOValidator
public class VisitorDTO implements Serializable {

    private Long id;
    private String name;
    private Long lessonId;
    private Instant createdAt;
    private Instant updatedAt;

}
