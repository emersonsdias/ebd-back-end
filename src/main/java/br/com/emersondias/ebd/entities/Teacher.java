package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "teachers")
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
    @Column(name = "teaching_period_start")
    private LocalDate teachingPeriodStart;
    @Column(name = "teaching_period_end")
    private LocalDate teachingPeriodEnd;
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
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

    public void updateFrom(Teacher other) {
        this.teachingPeriodStart = other.getTeachingPeriodStart();
        this.teachingPeriodEnd = other.getTeachingPeriodEnd();
        this.active = other.isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
