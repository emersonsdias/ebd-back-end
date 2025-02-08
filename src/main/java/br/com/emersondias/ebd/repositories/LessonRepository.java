package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
