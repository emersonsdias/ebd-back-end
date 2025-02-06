package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PersonDTO implements Serializable {

    private UUID id;
    private String name;
    private LocalDate birthdate;
    private String email;
    @Builder.Default
    private List<PhoneNumberDTO> phoneNumbers = new ArrayList<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
