package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "phone_numbers")
public class PhoneNumber implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
    @EqualsAndHashCode.Include
    @Column(name = "area_code", nullable = false)
    private String areaCode;
    @EqualsAndHashCode.Include
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(PhoneNumber other) {
        this.areaCode = other.getAreaCode();
        this.phoneNumber = other.getPhoneNumber();
        this.person = other.getPerson();
        this.active = other.isActive();
    }

}
