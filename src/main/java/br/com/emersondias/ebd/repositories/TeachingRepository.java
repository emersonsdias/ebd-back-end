package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Teaching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeachingRepository extends JpaRepository<Teaching, Long> {

    List<Teaching> findByTeacherPersonId(UUID personId);

}
