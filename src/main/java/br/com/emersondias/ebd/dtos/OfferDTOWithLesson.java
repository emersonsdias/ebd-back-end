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
public class OfferDTOWithLesson implements Serializable {

    private Long id;
    private BigDecimal amount;
    private LessonDTO lesson;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
