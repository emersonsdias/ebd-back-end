package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.dtos.location.CityDTO;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddressDTO implements Serializable {

    private UUID id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String zipCode;
    private CityDTO city;
    private Instant createdAt;
    private Instant updatedAt;

}
