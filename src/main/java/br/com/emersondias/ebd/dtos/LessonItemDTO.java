package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LessonItemDTO implements Serializable {

    private Long id;
    private Integer quantity;
    private Long lessonId;
    private Long itemId;
    private Instant createdAt;
    private Instant updatedAt;

}
