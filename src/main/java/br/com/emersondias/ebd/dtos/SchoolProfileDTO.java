package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.SchoolProfileDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@SchoolProfileDTOValidator
public class SchoolProfileDTO implements Serializable {

    private Long id;
    private String name;
    private String subtitle;
    private AddressDTO address;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
