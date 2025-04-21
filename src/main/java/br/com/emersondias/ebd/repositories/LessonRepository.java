package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

    List<Lesson> findByActiveTrue();

    List<Lesson> findByActiveTrueAndDateBetween(LocalDate startDate, LocalDate endDate);

}
