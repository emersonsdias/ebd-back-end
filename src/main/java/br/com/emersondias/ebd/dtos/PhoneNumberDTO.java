package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.PhoneNumberDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@PhoneNumberDTOValidator
public class PhoneNumberDTO implements Serializable {

    private UUID id;
    private String areaCode;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;

}
