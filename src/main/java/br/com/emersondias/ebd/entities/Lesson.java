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
    private Set<Attendance> attendances = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teaching> teachings = new HashSet<>();
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
                attendanceOpt.get().setItems(newAttendance.getItems());
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
