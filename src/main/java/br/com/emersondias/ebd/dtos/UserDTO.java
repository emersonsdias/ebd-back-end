package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.entities.enums.UserRole;
import br.com.emersondias.ebd.validations.annotations.UserDTOValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@UserDTOValidator
public class UserDTO implements Serializable {

    private UUID id;
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
