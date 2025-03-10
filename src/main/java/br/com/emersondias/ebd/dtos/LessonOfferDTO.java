package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.LessonOfferDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@LessonOfferDTOValidator
public class LessonOfferDTO implements Serializable {

    private Long id;
    private Long lessonId;
    private OfferDTO offer;
    private Instant createdAt;
    private Instant updatedAt;

}
