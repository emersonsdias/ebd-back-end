package br.com.emersondias.ebd.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@Table(schema = "app", name = "teachers")
public class Teacher extends ClassroomPerson {
}
