package br.com.emersondias.ebd.entities;

import br.com.emersondias.ebd.entities.enums.LessonStatus;
import br.com.emersondias.ebd.entities.enums.converters.LessonStatusConverter;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "lesson_number", nullable = false)
    private Integer number;
    @Column(name = "topic", nullable = false)
    private String topic;
    @Column(name = "lesson_date")
    private LocalDate date;
    @Convert(converter = LessonStatusConverter.class)
    @Column(name = "status")
    private LessonStatus status;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attendance> attendances = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teaching> teachings = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonItem> items = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visitor> visitors = new ArrayList<>();
    @Column(name = "active", nullable = false)
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(Lesson other) {
        this.number = other.getNumber();
        this.topic = other.getTopic();
        this.date = other.getDate();
        this.status = other.getStatus();
        this.notes = other.getNotes();
        this.classroom = other.getClassroom();
        setAttendances(other.getAttendances());
        setTeachings(other.getTeachings());
        setItems(other.getItems());
        setOffers(other.getOffers());
        setVisitors(other.getVisitors());
        this.active = other.isActive();
    }


    public void setItems(Set<LessonItem> items) {
        if (isNull(items)) {
            this.items.clear();
            return;
        }
        this.items.removeIf(li -> !items.contains(li));
        for (LessonItem newItem : items) {
            newItem.setLesson(this);
            if (this.items.contains(newItem)) {
                var itemOpt = this.items.stream()
                        .filter(newItem::equals)
                        .findFirst();
                if (itemOpt.isPresent()) {
                    itemOpt.get().updateFrom(newItem);
                } else {
                    this.items.add(newItem);
                }
            }
        }
    }

    public void setOffers(List<Offer> offers) {
        if (isNull(offers)) {
            this.offers.clear();
            return;
        }
        this.offers.removeIf(li -> !offers.contains(li));
        for (Offer newOffer : offers) {
            newOffer.setLesson(this);
            if (this.offers.contains(newOffer)) {
                var offerOpt = this.offers.stream()
                        .filter(newOffer::equals)
                        .findFirst();
                if (offerOpt.isPresent()) {
                    offerOpt.get().updateFrom(newOffer);
                } else {
                    this.offers.add(newOffer);
                }
            }
        }
    }

    public void setVisitors(Collection<Visitor> visitors) {
        if (isNull(visitors)) {
            this.visitors.clear();
            return;
        }
        this.visitors.removeIf(s -> !visitors.contains(s));
        for (Visitor newVisitor : visitors) {
            newVisitor.setLesson(this);
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

    public void setAttendances(Collection<Attendance> attendances) {
        if (isNull(attendances)) {
            this.attendances.clear();
            return;
        }
        this.attendances.removeIf(s -> !attendances.contains(s));
        for (Attendance newAttendance : attendances) {
            newAttendance.setLesson(this);
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

    public void setTeachings(Collection<Teaching> teachings) {
        if (isNull(teachings)) {
            this.teachings.clear();
            return;
        }
        this.teachings.removeIf(s -> !teachings.contains(s));
        for (Teaching newTeaching : teachings) {
            newTeaching.setLesson(this);
            var teachingOpt = this.teachings.stream()
                    .filter(newTeaching::equals)
                    .findFirst();
            if (teachingOpt.isPresent()) {
                teachingOpt.get().setTeacher(newTeaching.getTeacher());
                teachingOpt.get().setActive(newTeaching.isActive());
            } else {
                this.teachings.add(newTeaching);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
