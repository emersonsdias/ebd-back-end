package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.deserializer.CustomLocalDateDeserializer;
import br.com.emersondias.ebd.entities.enums.EducationLevel;
import br.com.emersondias.ebd.entities.enums.Gender;
import br.com.emersondias.ebd.entities.enums.MaritalStatus;
import br.com.emersondias.ebd.entities.enums.PersonType;
import br.com.emersondias.ebd.validations.annotations.PersonDTOValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@PersonDTOValidator
public class PersonDTO implements Serializable {

    private UUID id;
    private String name;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthdate;
    private String email;
    private Gender gender;
    private EducationLevel educationLevel;
    private MaritalStatus maritalStatus;
    private AddressDTO address;
    @Builder.Default
    private List<PhoneNumberDTO> phoneNumbers = new ArrayList<>();
    @Builder.Default
    private Set<PersonType> types = new HashSet<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
