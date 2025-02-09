package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.ItemDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ItemDTOValidator
public class ItemDTO implements Serializable {

    private Long id;
    private String name;
    private String icon;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
