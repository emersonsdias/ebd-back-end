package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SchoolProfileDTO implements Serializable {

    private Long id;
    private String name;
    private String subtitle;
    private AddressDTO address;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
