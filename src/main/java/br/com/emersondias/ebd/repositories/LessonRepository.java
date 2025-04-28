package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

    List<Lesson> findByActiveTrue();

    List<Lesson> findByActiveTrueAndDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
            select distinct l
            from Lesson l
            join l.classroom c
            join c.teachers t
            where t.person.id = :personId
            """)
    List<Lesson> findLessonsByTeacherPersonId(@Param("personId") UUID personId);
}
