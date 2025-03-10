package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByActiveTrue();

    List<Lesson> findByActiveTrueAndDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = """
            select *
            from app.lessons l
            where l.lesson_date < current_date
            order by l.lesson_date desc, l.id desc
            limit :limit
            """, nativeQuery = true)
    List<Lesson> findRecentLessons(Integer limit);
}
