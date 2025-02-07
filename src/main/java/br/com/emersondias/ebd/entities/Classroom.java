package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "app", name = "classrooms")
public class Classroom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers = new HashSet<>();
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();
    @Column(name = "active")
    private boolean active;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void setTeachers(Collection<Teacher> teachers) {
        if (isNull(teachers)) {
            this.teachers.clear();
            return;
        }
        this.teachers.removeIf(t -> !teachers.contains(t));
        for (Teacher newTeacher : teachers) {
            var phoneOpt = this.teachers.stream()
                    .filter(newTeacher::equals)
                    .findFirst();
            if (phoneOpt.isPresent()) {
                phoneOpt.get().setActive(newTeacher.isActive());
            } else {
                this.teachers.add(newTeacher);
            }
        }
    }

    public void setStudents(Collection<Student> students) {
        if (isNull(students)) {
            this.students.clear();
            return;
        }
        this.students.removeIf(s -> !students.contains(s));
        for (Student newStudent : students) {
            var phoneOpt = this.students.stream()
                    .filter(newStudent::equals)
                    .findFirst();
            if (phoneOpt.isPresent()) {
                phoneOpt.get().setActive(newStudent.isActive());
            } else {
                this.students.add(newStudent);
            }
        }
    }

}

