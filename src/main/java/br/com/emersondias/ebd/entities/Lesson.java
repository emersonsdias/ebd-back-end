package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
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
@Table(schema = "app", name = "lessons")
public class Lesson implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "lesson_number", nullable = false)
    private Integer lessonNumber;
    @Column(name = "lesson_date")
    private LocalDate lessonDate;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visitor> visitors = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonItem> items = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attendance> attendances = new HashSet<>();
    @Column(name = "active", nullable = false)
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void setVisitors(Collection<Visitor> visitors) {
        if (isNull(visitors)) {
            this.visitors.clear();
            return;
        }
        this.visitors.removeIf(s -> !visitors.contains(s));
        for (Visitor newVisitor : visitors) {
            var visitorOpt = this.visitors.stream()
                    .filter(newVisitor::equals)
                    .findFirst();
            if (visitorOpt.isPresent()) {
                visitorOpt.get().setName(newVisitor.getName());
            } else {
                this.visitors.add(newVisitor);
            }
        }
    }

    public void setOffers(Collection<Offer> offers) {
        if (isNull(offers)) {
            this.offers.clear();
            return;
        }
        this.offers.removeIf(s -> !offers.contains(s));
        for (Offer newOffer : offers) {
            var offerOpt = this.offers.stream()
                    .filter(newOffer::equals)
                    .findFirst();
            if (offerOpt.isPresent()) {
                offerOpt.get().setAmount(newOffer.getAmount());
                offerOpt.get().setActive(newOffer.isActive());
            } else {
                this.offers.add(newOffer);
            }
        }
    }

    public void setItems(Collection<LessonItem> items) {
        if (isNull(items)) {
            this.items.clear();
            return;
        }
        this.items.removeIf(s -> !items.contains(s));
        for (LessonItem newItem : items) {
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

    public void setAttendances(Collection<Attendance> attendances) {
        if (isNull(attendances)) {
            this.attendances.clear();
            return;
        }
        this.attendances.removeIf(s -> !attendances.contains(s));
        for (Attendance newAttendance : attendances) {
            var attendanceOpt = this.attendances.stream()
                    .filter(newAttendance::equals)
                    .findFirst();
            if (attendanceOpt.isPresent()) {
                attendanceOpt.get().setPresent(newAttendance.isPresent());
                attendanceOpt.get().setStudent(newAttendance.getStudent());
                attendanceOpt.get().setActive(newAttendance.isActive());
            } else {
                this.attendances.add(newAttendance);
            }
        }
    }

}
