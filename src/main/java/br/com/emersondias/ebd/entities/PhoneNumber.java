package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

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
    @Column(name = "area_code", nullable = false)
    private String areaCode;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
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
