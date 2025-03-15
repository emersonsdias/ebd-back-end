package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "teachings")
public class Teaching implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @Column(name = "active", nullable = false)
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(Teaching other) {
        this.teacher = other.getTeacher();
        this.lesson = other.getLesson();
        this.active = other.isActive();
    }

}
