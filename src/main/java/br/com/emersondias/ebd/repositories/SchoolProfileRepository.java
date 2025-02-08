package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.SchoolProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolProfileRepository extends JpaRepository<SchoolProfile, Long> {
}
