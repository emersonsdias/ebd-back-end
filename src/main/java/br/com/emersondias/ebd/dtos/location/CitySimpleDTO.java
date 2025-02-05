package br.com.emersondias.ebd.dtos.location;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CitySimpleDTO implements Serializable {

    private Long id;
    private String name;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
