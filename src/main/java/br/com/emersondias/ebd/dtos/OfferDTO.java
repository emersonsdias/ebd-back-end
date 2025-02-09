package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.OfferDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@OfferDTOValidator
public class OfferDTO implements Serializable {

    private Long id;
    private BigDecimal amount;
    private Long lessonId;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
