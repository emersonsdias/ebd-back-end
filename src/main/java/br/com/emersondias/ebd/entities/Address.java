package br.com.emersondias.ebd.entities;

import br.com.emersondias.ebd.entities.location.City;
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
@Table(schema = "app", name = "addresses")
public class Address implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
    @Column(name = "street")
    private String street;
    @Column(name = "number")
    private String number;
    @Column(name = "complement")
    private String complement;
    @Column(name = "neighborhood")
    private String neighborhood;
    @Column(name = "zipCode")
    private String zipCode;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(Address other) {
        this.street = other.getStreet();
        this.number = other.getNumber();
        this.complement = other.getComplement();
        this.neighborhood = other.getNeighborhood();
        this.zipCode = other.getZipCode();
        this.city = other.getCity();
        this.active = other.isActive();
    }

}
