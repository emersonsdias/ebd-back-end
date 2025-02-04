package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.validations.annotations.UserDTOValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@UserDTOValidator
public class UserDTO implements Serializable {

    private UUID id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    private String password;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
