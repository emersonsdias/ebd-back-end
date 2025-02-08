package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OfferDTO implements Serializable {

    private Long id;
    private BigDecimal amount;
    private Long lessonId;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
