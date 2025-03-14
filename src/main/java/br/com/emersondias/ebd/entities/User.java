package br.com.emersondias.ebd.entities;

import br.com.emersondias.ebd.entities.enums.UserRole;
import br.com.emersondias.ebd.entities.enums.converters.UserRoleConverter;
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

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "users")
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
    @Singular
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "app", name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Convert(converter = UserRoleConverter.class)
    @Column(name = "role")
    private Set<UserRole> roles = new HashSet<>();
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(User other) {
        this.name = other.getName();
        this.email = other.getEmail();
        if (Objects.nonNull(other.getPassword())) {
            this.password = other.getPassword();
        }
        this.roles = other.getRoles();
        this.active = other.isActive();
    }

    public void addRole(UserRole role) {
        if (isNull(roles)) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
