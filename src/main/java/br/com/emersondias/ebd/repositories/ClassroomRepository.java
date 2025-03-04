package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    List<Classroom> findByActiveTrue();

    List<Classroom> findByStudentsPersonId(UUID personId);

    List<Classroom> findByTeachersPersonId(UUID personId);

}
