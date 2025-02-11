package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findByPersonId(UUID id);

}
