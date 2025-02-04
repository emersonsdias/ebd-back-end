package br.com.emersondias.ebd.entities;

import br.com.emersondias.ebd.entities.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @ColumnTransformer(write = "LOWER(?)")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Singular
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Integer> roles = new HashSet<>();
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void setRoles(Set<UserRole> roles) {
        if (isNull(roles)) {
            this.roles = null;
            return;
        }
        this.roles = roles.stream().map(UserRole::getCod).collect(Collectors.toSet());
    }

    public Set<UserRole> getRoles() {
        if (isNull(roles)) {
            return new HashSet<>();
        }
        return roles.stream().map(UserRole::toEnum).collect(Collectors.toSet());
    }

    public void addRole(UserRole role) {
        if (isNull(roles)) {
            roles = new HashSet<>();
        }
        roles.add(role.getCod());
    }

}
