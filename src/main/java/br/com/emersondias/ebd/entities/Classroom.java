package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "classrooms")
public class Classroom implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "age_range_id")
    private AgeRange ageRange;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "classroom")
    private List<Lesson> lessons = new ArrayList<>();
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void updateFrom(Classroom other) {
        this.name = other.getName();
        this.ageRange = other.getAgeRange();
        this.setTeachers(other.getTeachers());
        this.setStudents(other.getStudents());
        this.setLessons(other.getLessons());
        this.active = other.isActive();
    }

    public void setTeachers(Set<Teacher> teachers) {
        if (isNull(teachers) || teachers.isEmpty()) {
            this.teachers.clear();
            return;
        }
        this.teachers.removeIf(t -> !teachers.contains(t));
        for (Teacher newTeacher : teachers) {
            newTeacher.setClassroom(this);
            if (this.teachers.contains(newTeacher)) {
                this.teachers.stream()
                        .filter(newTeacher::equals)
                        .findFirst().ifPresent(s -> s.updateFrom(newTeacher));
            } else {
                this.teachers.add(newTeacher);
            }
        }
    }

    public void setStudents(Set<Student> students) {
        if (isNull(students) || students.isEmpty()) {
            this.students.clear();
            return;
        }
        this.students.removeIf(s -> !students.contains(s));
        for (Student newStudent : students) {
            newStudent.setClassroom(this);
            if (this.students.contains(newStudent)) {
                this.students.stream()
                        .filter(newStudent::equals)
                        .findFirst().ifPresent(s -> s.updateFrom(newStudent));
            } else {
                this.students.add(newStudent);
            }
        }
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = new ArrayList<>(lessons);
    }

    public void addStudent(Student student) {
        if (isNull(this.students)) {
            this.students = new HashSet<>();
        }
        this.students.add(student);
    }

    public void addTeacher(Teacher teacher) {
        if (isNull(this.teachers)) {
            this.teachers = new HashSet<>();
        }
        this.teachers.add(teacher);
    }

}

