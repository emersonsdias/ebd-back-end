package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.VisitorDTOValidator;
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
@VisitorDTOValidator
public class VisitorDTO implements Serializable {

    private Long id;
    private String name;
    private Long lessonId;
    @Builder.Default
    private Set<VisitorItemDTO> items = new HashSet<>();
    @Builder.Default
    private Set<VisitorOfferDTO> offers = new HashSet<>();
    private Instant createdAt;
    private Instant updatedAt;

}
