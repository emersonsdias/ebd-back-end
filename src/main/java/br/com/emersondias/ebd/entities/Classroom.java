package br.com.emersondias.ebd.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static java.util.Objects.isNull;

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
    @Column(name = "active")
    private boolean active;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Builder.Default
    @OneToMany(mappedBy = "classroom")
    private List<Lesson> lessons = new ArrayList<>();

    public void setTeachers(Collection<Teacher> teachers) {
        if (isNull(teachers)) {
            this.teachers.clear();
            return;
        }
        this.teachers.removeIf(t -> !teachers.contains(t));
        for (Teacher newTeacher : teachers) {
            var teacherOpt = this.teachers.stream()
                    .filter(newTeacher::equals)
                    .findFirst();
            if (teacherOpt.isPresent()) {
                teacherOpt.get().setActive(newTeacher.isActive());
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
            var studentOpt = this.students.stream()
                    .filter(newStudent::equals)
                    .findFirst();
            if (studentOpt.isPresent()) {
                studentOpt.get().setActive(newStudent.isActive());
            } else {
                this.students.add(newStudent);
            }
        }
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

