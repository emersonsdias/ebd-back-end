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
@Table(schema = "app", name = "lessons_items")
public class LessonItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(LessonItem other) {
        this.quantity = other.getQuantity();
        this.item = other.getItem();
        this.active = other.isActive();
    }

}
