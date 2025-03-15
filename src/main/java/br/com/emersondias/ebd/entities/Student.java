package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "students")
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
    @Column(name = "academic_period_start")
    private LocalDate academicPeriodStart;
    @Column(name = "academic_period_end")
    private LocalDate academicPeriodEnd;
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;
    @Column(name = "active", nullable = false)
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(Student other) {
        this.academicPeriodStart = other.getAcademicPeriodStart();
        this.academicPeriodEnd = other.getAcademicPeriodEnd();
        this.active = other.isActive();
    }

}
