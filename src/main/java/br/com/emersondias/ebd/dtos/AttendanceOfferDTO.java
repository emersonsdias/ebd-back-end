package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.AttendanceOfferDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AttendanceOfferDTOValidator
public class AttendanceOfferDTO implements Serializable {

    private Long id;
    private Long attendanceId;
    private OfferDTO offer;
    private Instant createdAt;
    private Instant updatedAt;

}
