package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.AttendanceItemDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AttendanceItemDTOValidator
public class AttendanceItemDTO implements Serializable {

    private Long id;
    private Integer quantity;
    private Long attendanceId;
    private ItemDTO item;
    private Instant createdAt;
    private Instant updatedAt;

}
