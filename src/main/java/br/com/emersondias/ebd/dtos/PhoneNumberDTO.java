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

    public String getFormattedPhoneNumber() {
        String formattedPhoneNumber = "(" + areaCode + ") ";

        if (phoneNumber.length() == 8) {
            formattedPhoneNumber += phoneNumber.substring(0, 4) + "-" + phoneNumber.substring(4);
        } else if (phoneNumber.length() == 9) {
            formattedPhoneNumber += phoneNumber.substring(0, 5) + "-" + phoneNumber.substring(5);
        } else {
            formattedPhoneNumber += phoneNumber;
        }
        return formattedPhoneNumber;
    }

}
