package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VisitorDTO implements Serializable {

    private Long id;
    private String name;
    private Long lessonId;
    private Instant createdAt;
    private Instant updatedAt;

}
