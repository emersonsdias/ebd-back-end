package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByActiveTrue();

    List<Lesson> findByLessonDateBetween(LocalDate startDate, LocalDate endDate);
}
