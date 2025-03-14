package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.LessonItemDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@LessonItemDTOValidator
public class LessonItemDTO implements Serializable {

    private Long id;
    private Integer quantity;
    private Long lessonId;
    private ItemDTO item;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
