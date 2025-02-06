package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "people")
public class Person implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "birthdate")
    private LocalDate birthdate;
    @ColumnTransformer(write = "LOWER(?)")
    @Column(name = "email")
    private String email;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        if (isNull(phoneNumbers)) {
            this.phoneNumbers.clear();
            return;
        }
        this.phoneNumbers.removeIf(phoneNumber -> !phoneNumbers.contains(phoneNumber));
        for (PhoneNumber phoneNumber : phoneNumbers) {
            var phoneOpt = this.phoneNumbers.stream()
                    .filter(phoneNumber::equals)
                    .findFirst();
            if (phoneOpt.isPresent()) {
                phoneOpt.get().setAreaCode(phoneNumber.getAreaCode());
                phoneOpt.get().setPhoneNumber(phoneNumber.getPhoneNumber());
            } else {
                this.phoneNumbers.add(phoneNumber);
            }
        }
    }

}
