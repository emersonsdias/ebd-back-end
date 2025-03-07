package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(person, student.person) && Objects.equals(classroom, student.classroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, classroom);
    }
}
