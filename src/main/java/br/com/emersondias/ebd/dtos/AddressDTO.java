package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.dtos.location.CityDTO;
import br.com.emersondias.ebd.validations.annotations.AddressDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AddressDTOValidator
public class AddressDTO implements Serializable {

    private UUID id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String zipCode;
    private CityDTO city;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
