package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.entities.enums.EducationLevel;
import br.com.emersondias.ebd.entities.enums.Gender;
import br.com.emersondias.ebd.entities.enums.MaritalStatus;
import br.com.emersondias.ebd.validations.annotations.PersonDTOValidator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@PersonDTOValidator
public class PersonDTO implements Serializable {

    private UUID id;
    private String name;
    private LocalDate birthdate;
    private String email;
    private Gender gender;
    private EducationLevel educationLevel;
    private MaritalStatus maritalStatus;
    private AddressDTO address;
    @Builder.Default
    private List<PhoneNumberDTO> phoneNumbers = new ArrayList<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public String getFormattedPhoneNumbers() {
        if (this.getPhoneNumbers() == null || this.getPhoneNumbers().isEmpty()) {
            return "";
        }
        return this.getPhoneNumbers().stream()
                .map(PhoneNumberDTO::getFormattedPhoneNumber)
                .collect(Collectors.joining(" / "));
    }

}
