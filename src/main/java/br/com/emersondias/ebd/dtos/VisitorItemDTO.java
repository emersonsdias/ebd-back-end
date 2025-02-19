package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.VisitorItemDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@VisitorItemDTOValidator
public class VisitorItemDTO implements Serializable {

    private Long id;
    private Integer quantity;
    private Long visitorId;
    private Long itemId;
    private Instant createdAt;
    private Instant updatedAt;

}
