package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.VisitorOfferDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@VisitorOfferDTOValidator
public class VisitorOfferDTO implements Serializable {

    private Long id;
    private Long visitorId;
    private OfferDTO offer;
    private Instant createdAt;
    private Instant updatedAt;

}
