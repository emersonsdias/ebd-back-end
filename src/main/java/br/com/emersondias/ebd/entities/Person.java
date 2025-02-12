package br.com.emersondias.ebd.entities;

import br.com.emersondias.ebd.entities.enums.EducationLevel;
import br.com.emersondias.ebd.entities.enums.Gender;
import br.com.emersondias.ebd.entities.enums.MaritalStatus;
import br.com.emersondias.ebd.entities.enums.converters.EducationLevelConverter;
import br.com.emersondias.ebd.entities.enums.converters.GenderConverter;
import br.com.emersondias.ebd.entities.enums.converters.MaritalStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "people")
public class Person implements Serializable {

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
    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private Gender gender;
    @Convert(converter = EducationLevelConverter.class)
    @Column(name = "education_level")
    private EducationLevel educationLevel;
    @Convert(converter = MaritalStatusConverter.class)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
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

    public void setPhoneNumbers(Collection<PhoneNumber> phoneNumbers) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
