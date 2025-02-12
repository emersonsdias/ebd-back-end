package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentPersonId(UUID personId);
}
