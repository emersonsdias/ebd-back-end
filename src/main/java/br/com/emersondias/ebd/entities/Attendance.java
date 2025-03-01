package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "attendances")
public class Attendance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "present", nullable = false)
    private boolean present;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttendanceItem> items = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttendanceOffer> offers = new HashSet<>();
    @Column(name = "active", nullable = false)
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;


    public void setItems(Collection<AttendanceItem> items) {
        if (isNull(items)) {
            this.items.clear();
            return;
        }
        this.items.removeIf(s -> !items.contains(s));
        for (AttendanceItem newItem : items) {
            newItem.setAttendance(this);
            var itemOpt = this.items.stream()
                    .filter(newItem::equals)
                    .findFirst();
            if (itemOpt.isPresent()) {
                itemOpt.get().setItem(newItem.getItem());
                itemOpt.get().setQuantity(newItem.getQuantity());
            } else {
                this.items.add(newItem);
            }
        }
    }

    public void setOffers(Collection<AttendanceOffer> offers) {
        if (isNull(offers)) {
            this.offers.clear();
            return;
        }
        this.offers.removeIf(s -> !offers.contains(s));
        for (AttendanceOffer newOffer : offers) {
            newOffer.setAttendance(this);
            var offerOpt = this.offers.stream()
                    .filter(newOffer::equals)
                    .findFirst();
            if (offerOpt.isPresent()) {
                offerOpt.get().setOffer(newOffer.getOffer());
            } else {
                this.offers.add(newOffer);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(student, that.student) && Objects.equals(lesson, that.lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, lesson);
    }
}
