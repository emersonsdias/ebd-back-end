package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    List<Teacher> findByPersonId(UUID id);

}
